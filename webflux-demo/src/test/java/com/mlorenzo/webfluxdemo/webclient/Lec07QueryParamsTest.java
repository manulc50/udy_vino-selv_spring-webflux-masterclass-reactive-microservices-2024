package com.mlorenzo.webfluxdemo.webclient;

import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Lec07QueryParamsTest extends BaseTest {
	final static String QUERY = "htto://localhost:8080/jobs/search?count={count}&page={page}"; 

	@Autowired
	WebClient webClient;
	
	// 3 tests que son equivalentes pero utilizan diferentes métodos para establecer los Query Params
	
	@Test
	void queryParamsTest1() {
		URI uri = UriComponentsBuilder.fromUriString(QUERY).build(10, 20);
		
		Flux<Integer> integerFlux = webClient.get().uri(uri)
				.retrieve()
				.bodyToFlux(Integer.class) // Flux<Integer>
				.doOnNext(System.out::println); // Versión simplificada de la expresión "i -> System.out.println(i)"
	
		StepVerifier.create(integerFlux)
			.expectNext(10)
			.expectNext(20)
			.verifyComplete();
	}
	
	@Test
	void queryParamsTest2() {
		Flux<Integer> integerFlux = webClient.get()
				.uri(builder -> builder.path("jobs/search").query("count={count}&page={page}").build(10, 20))
				.retrieve()
				.bodyToFlux(Integer.class) // Flux<Integer>
				.doOnNext(System.out::println); // Versión simplificada de la expresión "i -> System.out.println(i)"
	
		StepVerifier.create(integerFlux)
			.expectNext(10)
			.expectNext(20)
			.verifyComplete();
	}
	
	@Test
	void queryParamsTest3() {
		// Una manera de crear Maps que apareció en Java 9
		Map<String, Integer> map = Map.of(
				"count", 10,
				"page", 20);
		
		Flux<Integer> integerFlux = webClient.get()
				.uri(builder -> builder.path("jobs/search").query("count={count}&page={page}").build(map))
				.retrieve()
				.bodyToFlux(Integer.class) // Flux<Integer>
				.log();
	
		StepVerifier.create(integerFlux)
			.expectNext(10)
			.expectNext(20)
			.verifyComplete();
	}
}
