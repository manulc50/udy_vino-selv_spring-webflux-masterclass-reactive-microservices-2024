package com.mlorenzo.webfluxdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
	
	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl("http://localhost:8080")
				// Si el destino de la petición http requiere credenciales mediante autenticación básica, podemos establecerlas de esta manera:
				//.defaultHeaders(h -> h.setBasicAuth("username", "password"))
				// Si el destino de la petición http requiere un Bearer Token(Como JWT) para realizar la autenticación, podemos establecerlo de esta manera:
				//.defaultHeaders(h -> h.setBearerAuth("some-token"))
				// Este método "filter" no es para realizar filtros a partir de un predicado que devuelva true o false como es habitual. Es para aplicar una función de Exchange para realizar alguna tarea dinámicamente en tiempo de ejecución
				// Como por ejemplo, a veces se necesita modificar una petición http para añadir una cabecera u otra dinámicamente en timepo de ejecución
				.filter(this::sessionToken) // Versión simplificada de la expresión "(clientRequest, exhangeFunction) -> this.sessionToken(clientRequest, exhangeFunction)"
				.build();
	}
	
	/*private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
		System.out.println("generating session token");
		// Debido a que cada ClientRequest es inmutable(sólo lectura), no se puede modificar
		// Por esta razón, creamos un nuevo ClientRequest a partir del ClientRequest original y realizamos la modificación que queramos(Añadir un Bearer Token a la cabecera)
		ClientRequest clientRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth("some-token")).build();
		return ex.exchange(clientRequest);
	}*/
	
	private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
		// auth -> basic or oauth
		// Si se encuentra un atributo "auth" con valor "basic", se establece una cabecera en la petición http con las credenciales para una autenticación básica
		// Si se encuentra un atributo "auth" con valor "oauth", se establece una cabecera en la petición http con una autenticación con Bearer Token
		// En caso contrario, no se establece ninguna cabecera en la petición http
		ClientRequest clientRequest = request.attribute("auth")
			.map(value -> value.equals("basic") ? withBasicAuth(request) : withOAuth(request))
			.orElse(request);
		
		return ex.exchange(clientRequest);
	}
	
	private ClientRequest withBasicAuth(ClientRequest request) {
		// Debido a que cada ClientRequest es inmutable(sólo lectura), no se puede modificar
		// Por esta razón, creamos un nuevo ClientRequest a partir del ClientRequest original y realizamos la modificación que queramos(Añadir credenciales para autenticación básica a la cabecera)
		return ClientRequest.from(request).headers(h -> h.setBasicAuth("username", "password")).build();
	}
	
	private ClientRequest withOAuth(ClientRequest request) {
		// Debido a que cada ClientRequest es inmutable(sólo lectura), no se puede modificar
		// Por esta razón, creamos un nuevo ClientRequest a partir del ClientRequest original y realizamos la modificación que queramos(Añadir un Bearer Token a la cabecera)
		return ClientRequest.from(request).headers(h -> h.setBearerAuth("some-token")).build();
	}
}
