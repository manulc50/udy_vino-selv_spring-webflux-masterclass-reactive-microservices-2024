package com.mlorenzo.reactiveproductservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mlorenzo.reactiveproductservice.dto.ProductDto;

import reactor.core.publisher.Sinks;


@Configuration
public class SinkConfig {
	
	@Bean
	public Sinks.Many<ProductDto> sink() {
		// Sink que emite varios items(si sólo se quiere emitir un único item, se tiene que usar el método "one" de la clase Sinks en lugar del método "many" de esa clase)
		// El método "replay" proporciona la capacidad de emitir los antiguos, o previos, items a los suscriptores que más han tardado en suscribirse
		// Por defecto, se emite todos los items disponibles pero se puede limitar la emisión de items usando el método "limit"
		return Sinks.many().replay().limit(1);
	}

}
