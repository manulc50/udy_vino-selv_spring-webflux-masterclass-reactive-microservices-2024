package com.mlorenzo.webfluxdemo.config;

import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mlorenzo.webfluxdemo.dto.InputFailedValidationResponse;
import com.mlorenzo.webfluxdemo.exception.InputValidationException;

import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig {
	
	@Autowired
	private RequestHandler requestHandler;

	// En una aplicación Spring Webflux es posible tener más de Router(componente RouterFunction que sea un bean de Spring)
	
	// Router con la ruta base
	// La ruta base es "/router"
	@Bean
	public RouterFunction<ServerResponse> highLevelRouter() {
		return RouterFunctions.route()
				.path("router", this::serverResponseRouterFunction) // Versión simplificada de la expresión "() -> this.serverResponseRouterFunction()"
				.build();
	}
	
	// Método con las subrutas del Router
	private RouterFunction<ServerResponse> serverResponseRouterFunction() {
		return RouterFunctions.route()
				/* ----------------------------- */
				// Si la ruta de la petición http coincide con "/square/" y el path variable "input" coincide con la expresión "*/1?"("/square/10", "/square/11",... , "/square/19") o con la expresión "*/20"("/square/20"), se ejecuta el método handler "squareHandler"
				// En caso contrario, si la ruta de la petición http coincide con "/square/" pero el path varible no coincide con la expresión "*/1? ni con la expresión "*/20" "(Por ejemplo, "/square/21" o "/square/9"), se envía la respuesta estado BAD_REQUEST(400) y el mensaje de error "only 10-19 allowed"
				.GET("square/{input}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")) ,requestHandler::squareHandler) // Versión simplificada de la expresión "request -> requestHandler.squareHandler(request)"
				.GET("square/{input}", request -> ServerResponse.badRequest().bodyValue("only 10-19 allowed"))
				/* ----------------------------- */
				.GET("table/{input}", requestHandler::tableHandler) // Versión simplificada de la expresión "request -> requestHandler.tableHandler(request)"
				.GET("table/{input}/stream", requestHandler::tableStreamHandler) // Versión simplificada de la expresión "request -> requestHandler.tableStreamHandler(request)"
				.POST("multiply", requestHandler::multiplyHandler) // Versión simplificada de la expresión "request -> requestHandler.multiplyHandler(request)"
				.GET("square/{input}/validation", requestHandler::squareHandlerWithValidation) // Versión simplificada de la expresión "request -> requestHandler.squareHandlerWithValidation(request)"
				// Si alguna de las rutas anteriores produce una excepción de tipo InputValidationException, la manejamos con el método exceptionHandler que devuelve en la respueta un objeto de tipo InputFailedValidationResponse con los datos de esa excepción
				.onError(InputValidationException.class, exceptionHandler())
				.build();
	}
	
	// Método que maneja excepciones de tipo InputValidationException
	private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
		return (err, req) -> {
			InputValidationException ex = (InputValidationException)err;
			InputFailedValidationResponse response = new InputFailedValidationResponse();
			response.setInput(ex.getInput());
			response.setMessage(ex.getMessage());
			response.setErrorCode(ex.getErrorCode());
			
			return ServerResponse.badRequest().bodyValue(response);
		};
	}
}
