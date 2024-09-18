package com.mlorenzo.reactiveproductservice.repository;

import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.mlorenzo.reactiveproductservice.entity.Product;

import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product, String>{

	// > min && < max - No incluye min ni max
	//Flux<Product> findByPriceBetween(int min, int max);
	
	// Si queremos incluir min y max, >= min && =< max, tenemos que usar Range
	Flux<Product> findByPriceBetween(Range<Integer> range);
}
