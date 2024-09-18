package com.mlorenzo.webfluxdemo.webtestclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.mlorenzo.webfluxdemo.config.RequestHandler;
import com.mlorenzo.webfluxdemo.config.RouterConfig;
import com.mlorenzo.webfluxdemo.dto.Response;

@WebFluxTest
@ContextConfiguration(classes = RouterConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec05RouterFunctionTest {
	
	// Nota: Si indicamos varias clases en la anotación @ContextConfiguration, en vez de inyectar sus beans correspondientes
	// uno por uno, es mejor inyectar directamente el bean de Spring correspondiente al Contexto de la Aplicación.
	//@Autowired
	//RouterConfig routerConfig;
	
	@Autowired
	ApplicationContext ctx;
	
	@MockBean
	RequestHandler requestHandler;
	
	WebTestClient webTestClient;
	
	@BeforeAll
	void setUp() {
		// Se comenta porque ahora asociamos el cliente WebTestClient al Contexto de la Aplicación.
		//webTestClient = WebTestClient.bindToRouterFunction(routerConfig.highLevelRouter()).build();
		webTestClient = WebTestClient.bindToApplicationContext(ctx).build();
	}

	@Test
	void test() {
		when(requestHandler.squareHandler(any())).thenReturn(ServerResponse.ok().bodyValue(new Response(225)));
	
		webTestClient.get()
			.uri("/router/square/{number}", 15)
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(Response.class)
			.value(resp -> assertThat(resp.getOutput()).isEqualTo(225));
	}
}
