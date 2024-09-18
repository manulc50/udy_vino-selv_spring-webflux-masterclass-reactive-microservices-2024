package com.mlorenzo.reactiveproductservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mlorenzo.reactiveproductservice.dto.ProductDto;
import com.mlorenzo.reactiveproductservice.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("all")
	public Flux<ProductDto> getAll() {
		return productService.getAll();
	}
	
	@GetMapping("price-range")
	public Flux<ProductDto> getProductsPriceRange(@RequestParam(name = "min") int min, @RequestParam(name = "max") int max) {
		return productService.getProductsByPriceRange(min, max);
	}
	
	@GetMapping("{id}")
	public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable(name = "id") String productId) {
		return productService.getProductById(productId)
				.map(ResponseEntity::ok) // Versión simplificada de la expresión "productDto -> ResponseEntity.ok(productDto)"
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ProductDto> insertProduct(@RequestBody Mono<ProductDto> productDto) {
		return productService.insertProduct(productDto);
	}
	
	@PutMapping("{id}")
	public Mono<ResponseEntity<ProductDto>> updateProduct(@PathVariable String id, @RequestBody Mono<ProductDto> productDto) {
		return productService.updateProduct(id, productDto)
				.map(ResponseEntity::ok) // Versión simplificada de la expresión "productDto -> ResponseEntity.ok(productDto)"
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteProduct(@PathVariable String id) {
		return productService.deleteProduct(id);
	}
	

}
