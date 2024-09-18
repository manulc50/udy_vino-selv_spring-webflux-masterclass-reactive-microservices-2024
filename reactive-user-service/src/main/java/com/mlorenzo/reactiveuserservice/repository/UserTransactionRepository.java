package com.mlorenzo.reactiveuserservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mlorenzo.reactiveuserservice.entity.UserTransaction;

import reactor.core.publisher.Flux;

public interface UserTransactionRepository extends ReactiveCrudRepository<UserTransaction, Integer>{
	Flux<UserTransaction> findByUserId(Integer userId);
}
