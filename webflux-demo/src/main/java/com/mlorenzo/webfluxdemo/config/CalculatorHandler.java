package com.mlorenzo.webfluxdemo.config;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

// Tarea(Assignment)

@Component
public class CalculatorHandler {

	// Para esta tarea hemos creados varios métodos handler de manera intencionada para probar varios Request Predicates sobre las cabeceras de las peticiones http en el Router
	
	public Mono<ServerResponse> additionHandler(ServerRequest request) {
		return process(request, (a, b) -> ServerResponse.ok().bodyValue(a + b));
	}
	
	public Mono<ServerResponse> subtractionHandler(ServerRequest request) {
		return process(request, (a, b) -> ServerResponse.ok().bodyValue(a - b));
	}
	
	public Mono<ServerResponse> multiplicationHandler(ServerRequest request) {
		return process(request, (a, b) -> ServerResponse.ok().bodyValue(a * b));
	}
	
	public Mono<ServerResponse> divisionHandler(ServerRequest request) {
		return process(request, (a, b) -> b != 0 ? ServerResponse.ok().bodyValue(a / b)
				: ServerResponse.badRequest().bodyValue("b can not be 0"));
	}
	
	private Mono<ServerResponse> process(ServerRequest request, BiFunction<Integer, Integer, Mono<ServerResponse>> opLogic) {
		int a = getValue(request, "a");
		int b = getValue(request, "b");
		return opLogic.apply(a, b);
	}
	
	private int getValue(ServerRequest request, String key) {
		return Integer.valueOf(request.pathVariable(key));
	}
}
