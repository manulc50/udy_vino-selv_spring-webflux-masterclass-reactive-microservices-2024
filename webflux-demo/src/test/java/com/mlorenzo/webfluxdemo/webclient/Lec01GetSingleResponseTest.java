package com.mlorenzo.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec01GetSingleResponseTest extends BaseTest {

	@Autowired
	WebClient webClient;
	
	@Test
	void blockTest() {
		Response response = webClient.get().uri("reactive-math/square/{input}", 5)
				.retrieve()
				.bodyToMono(Response.class) // Mono<Response>
				.block(); // En general, no se debe usar "block" porque es síncrono y bloquante. En los tests es mejor usar "Step Verifier" en lugar de "block" como veremos en los siguientes tests
		
		System.out.println(response);
	}
	
	@Test
	void stepVerifierTest() {
		Mono<Response> responseMono = webClient.get().uri("reactive-math/square/{input}", 5)
				.retrieve()
				.bodyToMono(Response.class); // Mono<Response>
		
		StepVerifier.create(responseMono)
			.expectNextMatches(response -> response.getOutput() == 25)
			.verifyComplete();
	}
	
}
