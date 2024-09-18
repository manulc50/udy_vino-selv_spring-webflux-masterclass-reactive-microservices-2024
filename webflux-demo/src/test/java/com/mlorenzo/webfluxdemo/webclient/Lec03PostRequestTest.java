package com.mlorenzo.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.mlorenzo.webfluxdemo.dto.MultiplyRequestDto;
import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec03PostRequestTest extends BaseTest {

	@Autowired
	WebClient webClient;
	
	@Test
	void postTest() {
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
