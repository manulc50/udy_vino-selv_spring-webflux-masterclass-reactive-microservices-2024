package com.mlorenzo.reactiveorderservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mlorenzo.reactiveorderservice.dto.TransactionRequestDto;
import com.mlorenzo.reactiveorderservice.dto.TransactionResponseDto;
import com.mlorenzo.reactiveorderservice.dto.UserDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClient {
	private final WebClient webClient;
	
	public UserClient(@Value("${user.service.url}") String url) {
		webClient = WebClient.builder()
				.baseUrl(url)
				.build();
	}
	
	public Mono<TransactionResponseDto> authorizeTransaction(TransactionRequestDto requestDto) {
		return webClient.post()
				.uri("transaction")
				.bodyValue(requestDto)
				.retrieve()
				.bodyToMono(TransactionResponseDto.class);
	}
	
	public Flux<UserDto> getAllUsers() {
		return webClient.get()
				.uri("all")
				.retrieve()
				.bodyToFlux(UserDto.class);
	}

}
