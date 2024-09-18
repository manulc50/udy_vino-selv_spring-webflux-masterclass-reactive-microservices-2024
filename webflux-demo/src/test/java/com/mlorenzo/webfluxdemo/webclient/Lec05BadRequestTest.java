package com.mlorenzo.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec05BadRequestTest extends BaseTest {
	
	@Autowired
	WebClient webClient;
	
	@Test
	void badRequestTest() {
		Mono<Response> responseMono = webClient.get().uri("reactive-math/square/{number}/throw", 5)
				.retrieve()
				.bodyToMono(Response.class) // Mono<Response>
				.doOnError(err -> System.out.println(err.getMessage()));
		
		StepVerifier.create(responseMono)
			.verifyError(WebClientResponseException.BadRequest.class);
	}
}
