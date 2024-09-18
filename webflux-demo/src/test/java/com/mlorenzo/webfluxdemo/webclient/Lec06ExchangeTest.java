package com.mlorenzo.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.mlorenzo.webfluxdemo.dto.InputFailedValidationResponse;
import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec06ExchangeTest extends BaseTest {

	@Autowired
	WebClient webClient;
	
	// exhange = retrieve +  additional info like http status code
	@Test
	void exhangeTest() {
		Mono<Object> responseMono = webClient.get().uri("reactive-math/square/{number}/throw", 5)
				.exchangeToMono(this::exchange) // Versión simplificada de la expresión "cr -> this.exchange(cr)"
				.log();
		
		StepVerifier.create(responseMono)
			.expectNextCount(1)
			.verifyComplete();
	}
	
	private Mono<Object> exchange(ClientResponse cr) {
		if(cr.rawStatusCode() == 400)
			return cr.bodyToMono(InputFailedValidationResponse.class);
		else
			return cr.bodyToMono(Response.class);	
	}
}
