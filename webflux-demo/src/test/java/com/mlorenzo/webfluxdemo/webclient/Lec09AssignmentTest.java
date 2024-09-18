package com.mlorenzo.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

// Tarea(Assignment)

class Lec09AssignmentTest extends BaseTest {
	static final String FORMAT = "%d %s %d = %d";
	static final int A = 10;

	@Autowired
	WebClient webClient;
	
	@Test
	void calculatorTest() {
		Flux<String> stringFlux = Flux.range(1, 5)
				.flatMap(b -> Flux.just("+","-","*","/")
						.flatMap(op -> send(b, op)))
				.doOnNext(System.out::println); // Versión simplificada de la expresión "result -> System.out.println(result)"
	
		StepVerifier.create(stringFlux)
			.expectNextCount(20)
			.verifyComplete();
	}
	
	private Mono<String> send(int b, String op) {
		return webClient.get().uri("calculator/{a}/{b}",A, b)
				.headers(h -> h.set("OP", op))
				.retrieve()
				.bodyToMono(Integer.class)
				.map(result -> String.format(FORMAT, A, op, b, result));
	}
}
