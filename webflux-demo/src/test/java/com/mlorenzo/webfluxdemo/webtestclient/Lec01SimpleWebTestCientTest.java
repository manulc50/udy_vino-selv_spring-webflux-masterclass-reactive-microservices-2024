package com.mlorenzo.webfluxdemo.webtestclient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
class Lec01SimpleWebTestCientTest {
	
	@Autowired
	WebTestClient webTestClient;

	@Test
	void stepVerifierTest() {
		Flux<Response> responseFlux = webTestClient.get().uri("/reactive-math/square/{input}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.returnResult(Response.class)
				.getResponseBody();
		
		StepVerifier.create(responseFlux)
			.expectNextMatches(response -> response.getOutput() == 25)
			.verifyComplete();
	}
	
	@Test
	void fluentAssertionTest() {
		webTestClient.get().uri("/reactive-math/square/{input}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Response.class)
				.value(r -> assertThat(r.getOutput()).isEqualTo(25));
	}
}
