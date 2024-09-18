package com.mlorenzo.reactiveuserservice.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mlorenzo.reactiveuserservice.entity.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer>{

	// Como esta Query se trata de una modificación de una tabla de la base de datos y no se trata de una consulta, tenemos que usar la anotacion @Modifying para indicarlo
	@Modifying 
	@Query(
			"UPDATE users" + 
			" SET balance = balance - :amount" +
			" WHERE id = :userId" +
			" AND balance >= :amount"
	)
	Mono<Boolean> updateUserBalance(int userId, int amount);
}
