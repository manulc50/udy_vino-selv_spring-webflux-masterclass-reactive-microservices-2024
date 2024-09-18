package com.mlorenzo.webfluxdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlorenzo.webfluxdemo.dto.Response;
import com.mlorenzo.webfluxdemo.exception.InputValidationException;
import com.mlorenzo.webfluxdemo.service.ReactiveMathService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive-math")
public class ReactiveMathValidationController {
	
	@Autowired
	private ReactiveMathService reactiveMathService;
	
	// Validación no reactiva. El código de la parte de la validación no está en el pipeline reactivo
	@GetMapping("square/{input}/throw")
	public Mono<Response> findSquare(@PathVariable int input) {
		// Se lanza una excepción fuera del pipeline reactivo
		if(input < 10 || input > 20)
			throw new InputValidationException(input);
			
		return this.reactiveMathService.findSquare(input);
	}
	
	// Validación reactiva y recomendada porque todo el código está dentro del pipeline reactivo
	@GetMapping("square/{input}/mono-error")
	public Mono<Response> monoError(@PathVariable int input) {
		return Mono.just(input)
				.handle((i, sink) -> {
					// Si el número del argumento de entrada está en el rango permitido según la lógica de negocio, se envía una señal "next" para que continue el flujo
					if(input >= 10 && input <= 20)
						sink.next(input);
					// En caso contrario, es decir, si está fuera del rango permitido, se envía una señal "error" con una excepción de tipo InputValidationException que hace que se interrumpa el flujo 
					else
						sink.error(new InputValidationException(input));
				})
				// Como el método "handler" devuelve un Mono de tipo Object y se está tratando un número entero, hacemos un casting a Integer para obtener al final un Mono de tipo Integer
				.cast(Integer.class)
				.flatMap(i -> reactiveMathService.findSquare(i));
	}
	
	// Tarea(Assignment): Otra manera totalmente reactiva sin usar excepciones, ni señales de error, ni manejadores de excepciones
	@GetMapping("square/{input}/assignment")
	public Mono<ResponseEntity<Response>> assignment(@PathVariable int input) {
		return Mono.just(input)
				.filter(i -> i >= 10 && i <= 20)
				.flatMap(i -> reactiveMathService.findSquare(i))
				.map(ResponseEntity::ok) // Versión simplificada dela expresión "response -> ResponseEntity.ok(response)"
				.defaultIfEmpty(ResponseEntity.badRequest().build());				
	}

}
