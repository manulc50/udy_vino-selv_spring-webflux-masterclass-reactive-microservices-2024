package com.mlorenzo.reactiveproductservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;

import com.mlorenzo.reactiveproductservice.dto.ProductDto;
import com.mlorenzo.reactiveproductservice.repository.ProductRepository;
import com.mlorenzo.reactiveproductservice.util.EntityDtoUtil;

import io.netty.util.internal.ThreadLocalRandom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private Sinks.Many<ProductDto> sink;
	
	public Flux<ProductDto> getAll(){
		return productRepository.findAll()
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "product -> EntityDtoUtil.toDto(product)"
	}
	
	public Flux<ProductDto> getProductsByPriceRange(int min, int max) {
		// Usamos el método "close" de Range porque queremos incluir "min" y "max" en el rango
		return productRepository.findByPriceBetween(Range.closed(min,max))
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "product -> EntityDtoUtil.toDto(product)"
	}
	
	public Mono<ProductDto> getProductById(String id) {
		simulateRandomException(); // Lanza exceptiones de tipo RuntimeException de forma aleatoria para probar la resiliencia
		return productRepository.findById(id)
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "product -> EntityDtoUtil.toDto(product)"
	}
	
	public Mono<ProductDto> insertProduct(Mono<ProductDto> productDto) {
		return productDto
				.map(EntityDtoUtil::toEntity) // Versión simplificada de la expresión "dto -> EntityDtoUtil.toEntity(dto)"
				.flatMap(productRepository::insert) // Versión simplificada de la expresión "entity -> productRepository.insert(entity)"
				.map(EntityDtoUtil::toDto) // Versión simplificada de la expresión "entity -> EntityDtoUtil.toDto(entity)"
				.doOnNext(sink::tryEmitNext); // Versión simplificada de la expresión "pDto -> sink.tryEmitNext(pDto)"
	}
	
	public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDto) {
		return productRepository.findById(id)
				.flatMap(product -> productDto
						.map(EntityDtoUtil::toEntity)) // Versión simplificada de la expresión "dto -> EntityDtoUtil.toEntity(dto)"
						.doOnNext(entity -> entity.setId(id))
				.flatMap(productRepository::save) // Versión simplificada de la expresión "entity -> productRepository.save(entity)"
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "entity -> EntityDtoUtil.toDto(entity)"
				
	}
	
	public Mono<Void> deleteProduct(String id) {
		return productRepository.deleteById(id);
	}
	
	private void simulateRandomException() {
		// Genera un número aleatorio entero entre 1 y 10
		int nextInt = ThreadLocalRandom.current().nextInt(1, 10);
		if(nextInt > 5)
			throw new RuntimeException("something is wrong");
	}
}
