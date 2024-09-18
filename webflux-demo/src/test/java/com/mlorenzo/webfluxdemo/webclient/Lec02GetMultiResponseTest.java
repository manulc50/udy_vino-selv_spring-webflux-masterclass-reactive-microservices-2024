package com.mlorenzo.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Lec02GetMultiResponseTest extends BaseTest {

	@Autowired
	WebClient webClient;
	
	@Test
	void fluxTest() {
		Flux<Response> responseFlux = webClient.get().uri("reactive-math/table/{input}", 5)
				.retrieve()
				.bodyToFlux(Response.class) // Flux<Response>
				.log();
		
		StepVerifier.create(responseFlux)
			.expectNextCount(10)
			.verifyComplete();
	}
	
	@Test
	void fluxStreamTest() {
		Flux<Response> responseFlux = webClient.get().uri("reactive-math/table/{input}/stream", 5)
				.retrieve()
				.bodyToFlux(Response.class) // Flux<Response>
				.log();
		
		StepVerifier.create(responseFlux)
			.expectNextCount(10)
			.verifyComplete();
	}
}
