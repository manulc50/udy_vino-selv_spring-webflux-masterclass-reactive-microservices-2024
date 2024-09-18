package com.mlorenzo.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.mlorenzo.webfluxdemo.dto.MultiplyRequestDto;
import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec08AttributesTest extends BaseTest {

	@Autowired
	private WebClient webClient;
	
	@Test
	void attributeBasicTest() {
		Mono<Response> responseMono = webClient.post().uri("reactive-math/multiply")
				.bodyValue(buildRequestDto(5, 2))
				.attribute("auth", "basic")
				.retrieve()
				.bodyToMono(Response.class)
				.log();
		
		StepVerifier.create(responseMono)
			.expectNextMatches(response -> response.getOutput() == 10)
			.verifyComplete();
	}
	
	@Test
	void attributeOAuthTest() {
		Mono<Response> responseMono = webClient.post().uri("reactive-math/multiply")
				.bodyValue(buildRequestDto(5, 2))
				.attribute("auth", "oauth")
				.retrieve()
				.bodyToMono(Response.class)
				.log();
		
		StepVerifier.create(responseMono)
			.expectNextMatches(response -> response.getOutput() == 10)
			.verifyComplete();
	}
	
	@Test
	void withOutAttributeTest() {
		Mono<Response> responseMono = webClient.post().uri("reactive-math/multiply")
				.bodyValue(buildRequestDto(5, 2))
				.retrieve()
				.bodyToMono(Response.class)
				.log();
		
		StepVerifier.create(responseMono)
			.expectNextMatches(response -> response.getOutput() == 10)
			.verifyComplete();
	}
	
	private MultiplyRequestDto buildRequestDto(int a, int b) {
		MultiplyRequestDto dto = new MultiplyRequestDto();
		dto.setFirst(a);
		dto.setSecond(b);
		return dto;
	}
}
