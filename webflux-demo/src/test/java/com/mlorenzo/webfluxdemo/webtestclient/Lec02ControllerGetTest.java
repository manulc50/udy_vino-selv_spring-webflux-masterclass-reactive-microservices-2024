package com.mlorenzo.webfluxdemo.webtestclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mlorenzo.webfluxdemo.controller.ParamsController;
import com.mlorenzo.webfluxdemo.controller.ReactiveMathController;
import com.mlorenzo.webfluxdemo.dto.Response;
import com.mlorenzo.webfluxdemo.service.ReactiveMathService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Esta anotación también incluye la anotación @AutoConfigureWebTestClient para crear y autoconfigurar un bean
// de Spring de tipo WebTestClient
@WebFluxTest(controllers = { ReactiveMathController.class, ParamsController.class })
class Lec02ControllerGetTest {

	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	ReactiveMathService reactiveMathService;
	
	@Test
	void singleResponseTest() {
		when(reactiveMathService.findSquare(anyInt())).thenReturn(Mono.just(new Response(25)));
		
		webTestClient.get().uri("/reactive-math/square/{input}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Response.class)
				.value(r -> assertThat(r.getOutput()).isEqualTo(25));
	}
	
	@Test
	void listResponseTest() {
		Flux<Response> fluxReponse = Flux.range(1,  3)
			.map(Response::new); // Versión simplificada de la expresión "num -> new Response(num)"
		
		when(reactiveMathService.multiplicationTable(anyInt())).thenReturn(fluxReponse);
		
		webTestClient.get().uri("/reactive-math/table/{input}", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Response.class)
				.hasSize(3);
	}
	
	@Test
	void streamingResponseTest() {
		Flux<Response> fluxReponse = Flux.range(1,  3)
			.map(Response::new) // Versión simplificada de la expresión "num -> new Response(num)"
			.delayElements(Duration.ofMillis(100));
			
		when(reactiveMathService.multiplicationTable(anyInt())).thenReturn(fluxReponse);
		
		webTestClient.get().uri("/reactive-math/table/{input}/stream", 5)
				.exchange()
				.expectStatus().is2xxSuccessful()
				// Usamos este método "contentTypeCompatibleWith", en vez del método "contentType", para que acepte el contenido de la
				// cabecera "text/event-stream;charset=UTF-8". Es decir, si usamos el método "contentType", el contenido tiene que ser
				// exactamente "text/event-stream" para que pase la validación.
				.expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
				.expectBodyList(Response.class)
				.hasSize(3);
	}
	
	@Test
	void paramTest() {
		Map<String, Integer> map = Map.of(
				"count", 10,
				"page", 20);
		
		webTestClient.get()
				.uri(builder -> builder.path("/jobs/search").query("count={count}&page={page}").build(map))
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBodyList(Integer.class)
				.hasSize(2)
				.contains(10, 20);
	}
}
