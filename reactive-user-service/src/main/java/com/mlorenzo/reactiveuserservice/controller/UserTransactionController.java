package com.mlorenzo.reactiveuserservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mlorenzo.reactiveuserservice.dto.TransactionRequestDto;
import com.mlorenzo.reactiveuserservice.dto.TransactionResponseDto;
import com.mlorenzo.reactiveuserservice.dto.UserTransactionDto;
import com.mlorenzo.reactiveuserservice.service.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user/transaction")
public class UserTransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<TransactionResponseDto> createTransaction(@RequestBody Mono<TransactionRequestDto> requestDtoMono) {
		return requestDtoMono.flatMap(transactionService::createTransaction); // Versión simplificada de la expresión "requestDto -> transactionService.createTransaction(requestDto)"
	}
	
	@GetMapping()
	public Flux<UserTransactionDto> getByUserId(@RequestParam int userId) {
		return transactionService.getByUserId(userId);
	}

}
