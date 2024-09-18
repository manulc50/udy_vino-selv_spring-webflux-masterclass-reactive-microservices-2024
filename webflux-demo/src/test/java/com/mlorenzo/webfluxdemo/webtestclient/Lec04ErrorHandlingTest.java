package com.mlorenzo.webfluxdemo.webtestclient;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mlorenzo.webfluxdemo.controller.ReactiveMathValidationController;
import com.mlorenzo.webfluxdemo.dto.Response;
import com.mlorenzo.webfluxdemo.service.ReactiveMathService;

import reactor.core.publisher.Mono;

@WebFluxTest(ReactiveMathValidationController.class)
class Lec04ErrorHandlingTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	ReactiveMathService reactiveMathService;
	
	@Test
	void errorHandlingTest() {
		when(reactiveMathService.findSquare(anyInt())).thenReturn(Mono.just(new Response(1)));
		
		webTestClient.get()
			.uri("/reactive-math/square/{number}/throw", 5)
			.exchange()
			.expectStatus().isBadRequest()
			.expectBody()
			.jsonPath("$.message").isEqualTo("Allowed range is 10 - 20")
			.jsonPath("$.errorCode").isEqualTo(100)
			.jsonPath("$.input").isEqualTo(5);
	}

}
