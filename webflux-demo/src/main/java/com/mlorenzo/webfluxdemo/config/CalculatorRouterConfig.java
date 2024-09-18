package com.mlorenzo.webfluxdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CalculatorRouterConfig {

	@Autowired
	private CalculatorHandler calculatorHandler;
	
	// En una aplicación Spring Webflux es posible tener más de Router(componente RouterFunction que sea un bean de Spring)
	
	// Router con la ruta base
	// La ruta base es "/calculator"
	@Bean
	public RouterFunction<ServerResponse> highLevelCalculatorRouter() {
		return RouterFunctions.route()
				.path("calculator", this::serverResponseRouterFunction) // Versión simplificada de la expresión "() -> this.serverResponseRouterFunction()"
				.build();
	}
	
	// Método con las subrutas del Router
	// Probamos Request Predicates sobre las cabeceras de las peticiones http
	private RouterFunction<ServerResponse> serverResponseRouterFunction() {
		return RouterFunctions.route()
				// Si la ruta de la petición http coincide con "{a}/{b}"(2 paths variables, "a" y "b") y esa petición tiene una cabecera "OP" con el valor "+", se ejecuta el método handler "additionHandler"
				.GET("{a}/{b}", isOperation("+"), calculatorHandler::additionHandler) // Versión simplificada de la expresión "request -> calculatorHandler.additionHandler(request)"
				// Si la ruta de la petición http coincide con "{a}/{b}"(2 paths variables, "a" y "b") y esa petición tiene una cabecera "OP" con el valor "-", se ejecuta el método handler "subtractionHandler"
				.GET("{a}/{b}", isOperation("-"), calculatorHandler::subtractionHandler) // Versión simplificada de la expresión "request -> calculatorHandler.subtractionHandler(request)"
				// Si la ruta de la petición http coincide con "{a}/{b}"(2 paths variables, "a" y "b") y esa petición tiene una cabecera "OP" con el valor "*", se ejecuta el método handler "multiplicationHandler"
				.GET("{a}/{b}", isOperation("*"), calculatorHandler::multiplicationHandler) // Versión simplificada de la expresión "request -> calculatorHandler.multiplicationHandler(request)"
				// Si la ruta de la petición http coincide con "{a}/{b}"(2 paths variables, "a" y "b") y esa petición tiene una cabecera "OP" con el valor "/", se ejecuta el método handler "divisionHandler"
				.GET("{a}/{b}", isOperation("/"), calculatorHandler::divisionHandler) // Versión simplificada de la expresión "request -> calculatorHandler.divisionHandler(request)"
				// En caso contrario, si la ruta de la petición http coincide con "{a}/{b}"(2 paths variables, "a" y "b") pero esa petición no contiene una cabecera "OP" con "+", "-", "*" o "/", se envía la respuesta estado BAD_REQUEST(400) y el mensaje de error "OP should be +, -, *, /"
				.GET("{a}/{b}", request -> ServerResponse.badRequest().bodyValue("OP should be +, -, *, /"))
				.build();
	}
	
	// Método que comprueba si en la petición http hay una cabecera con key o parámetro "OP" cuyo valor sea el texto contenido en "operation"(Puede ser "+", "-", "*", "/")
	private RequestPredicate isOperation(String operation) {
		return RequestPredicates.headers(header -> operation.equals(header.asHttpHeaders().toSingleValueMap().get("OP")));
	}
}
