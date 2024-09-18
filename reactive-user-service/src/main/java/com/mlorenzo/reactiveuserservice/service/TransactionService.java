package com.mlorenzo.reactiveuserservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mlorenzo.reactiveuserservice.dto.TransactionRequestDto;
import com.mlorenzo.reactiveuserservice.dto.TransactionResponseDto;
import com.mlorenzo.reactiveuserservice.dto.TransactionStatus;
import com.mlorenzo.reactiveuserservice.dto.UserTransactionDto;
import com.mlorenzo.reactiveuserservice.repository.UserRepository;
import com.mlorenzo.reactiveuserservice.repository.UserTransactionRepository;
import com.mlorenzo.reactiveuserservice.util.EntityDtoUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserTransactionRepository transactionRepository;
	
	public Mono<TransactionResponseDto> createTransaction(TransactionRequestDto requestDto) {
		return userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
			.filter(Boolean::booleanValue) // Versión simplificada de la expresión "response -> response.booleanValue()"
			.map(bool -> EntityDtoUtil.toEntity(requestDto))
			.flatMap(transactionRepository::save) // Versión simplificada de la expresión "ut -> transactionRepository.save(ut)"
			.map(ut -> EntityDtoUtil.toDto(ut, TransactionStatus.APPROVED))
			.defaultIfEmpty(new TransactionResponseDto(requestDto.getUserId(), requestDto.getAmount(), TransactionStatus.DECLINE));
	}
	
	public Flux<UserTransactionDto> getByUserId(int userId) {
		return transactionRepository.findByUserId(userId)
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "ut -> EntityDtoUtil.toDto(ut)"
	}

}
