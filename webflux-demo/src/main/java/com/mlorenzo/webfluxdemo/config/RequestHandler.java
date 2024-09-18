package com.mlorenzo.webfluxdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mlorenzo.webfluxdemo.dto.MultiplyRequestDto;
import com.mlorenzo.webfluxdemo.dto.Response;
import com.mlorenzo.webfluxdemo.exception.InputValidationException;
import com.mlorenzo.webfluxdemo.service.ReactiveMathService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RequestHandler {

	@Autowired
	private ReactiveMathService reactiveMathService;
	
	public Mono<ServerResponse> squareHandler(ServerRequest request) {
		int input = Integer.valueOf(request.pathVariable("input"));
		Mono<Response> responseMono = reactiveMathService.findSquare(input);
		return ServerResponse.ok().body(responseMono, Response.class);
	}
	
	public Mono<ServerResponse> tableHandler(ServerRequest request) {
		int input = Integer.valueOf(request.pathVariable("input"));
		Flux<Response> responseFlux = reactiveMathService.multiplicationTable(input);
		return ServerResponse.ok().body(responseFlux, Response.class);
	}
	
	public Mono<ServerResponse> tableStreamHandler(ServerRequest request) {
		int input = Integer.valueOf(request.pathVariable("input"));
		Flux<Response> responseFlux = reactiveMathService.multiplicationTable(input);
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(responseFlux, Response.class);
	}
	
	public Mono<ServerResponse> multiplyHandler(ServerRequest request) {
		Mono<MultiplyRequestDto> requestDtoMono =  request.bodyToMono(MultiplyRequestDto.class);
		Mono<Response> responseMono = reactiveMathService.multiply(requestDtoMono);
		return ServerResponse.ok().body(responseMono, Response.class);
	}
	
	public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest request) {
		int input = Integer.valueOf(request.pathVariable("input"));
		if(input < 10 || input > 20) {
			// Devuelve directamente una respuesta de estado de error BAD_REQUEST(400)
			//return ServerResponse.badRequest().bodyValue(new InputFailedValidationResponse());
			// Devuelve un flujo reactivo Mono de error con una excepción de tipo InputValidationException. Esta excepción será manejada en la clase Router RouterConfig
			return Mono.error(new InputValidationException(input));
		}
		
		Mono<Response> responseMono = reactiveMathService.findSquare(input);
		return ServerResponse.ok().body(responseMono, Response.class);
	}
}
