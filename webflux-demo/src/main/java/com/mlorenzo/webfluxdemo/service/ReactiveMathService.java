package com.mlorenzo.webfluxdemo.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.mlorenzo.webfluxdemo.dto.MultiplyRequestDto;
import com.mlorenzo.webfluxdemo.dto.Response;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveMathService {
	
	public Mono<Response> findSquare(int input) {
		// Dos maneras de hacerlo:
		//return Mono.just(new Response(input * input));
		return Mono.fromSupplier(() -> new Response(input * input));
	}
	
	public Flux<Response> multiplicationTable(int input) {
		return Flux.range(1, 10)
				//.doOnNext(i -> SleepUtil.sleepSeconds(1)) // Sleep bloqueante
				.delayElements(Duration.ofSeconds(1)) // Sleep no bloquante
				.doOnNext(i -> System.out.println("reactive-math-service procesing: " + i))
				.map(i -> new Response(i * input));
	}
	
	public Mono<Response> multiply(Mono<MultiplyRequestDto> dtoMono) {
		return dtoMono
				.map(dto -> new Response(dto.getFirst() * dto.getSecond()));
	}
}
