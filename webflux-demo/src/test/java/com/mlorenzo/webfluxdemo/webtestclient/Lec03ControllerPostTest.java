package com.mlorenzo.webfluxdemo.webtestclient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mlorenzo.webfluxdemo.controller.ReactiveMathController;
import com.mlorenzo.webfluxdemo.dto.MultiplyRequestDto;
import com.mlorenzo.webfluxdemo.dto.Response;
import com.mlorenzo.webfluxdemo.service.ReactiveMathService;

import reactor.core.publisher.Mono;

@WebFluxTest(ReactiveMathController.class)
class Lec03ControllerPostTest {

	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	ReactiveMathService reactiveMathService;
	
	@Test
	void postTest() {
		when(reactiveMathService.multiply(any())).thenReturn(Mono.just(new Response(1)));
		
		webTestClient.post()
			.uri("/reactive-math/multiply")
			.accept(MediaType.APPLICATION_JSON)
			.headers(h -> h.setBasicAuth("username", "password"))
			.headers(h -> h.set("somekey", "somevalue"))
			.bodyValue(new MultiplyRequestDto())
			.exchange()
			.expectStatus().is2xxSuccessful();	
	}
}
