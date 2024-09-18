package com.mlorenzo.reactiveproductservice.bootstrap;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mlorenzo.reactiveproductservice.dto.ProductDto;
import com.mlorenzo.reactiveproductservice.service.ProductService;

import io.netty.util.internal.ThreadLocalRandom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DataSetup implements CommandLineRunner {

	@Autowired
	private ProductService productService;
	
	@Override
	public void run(String... args) throws Exception {
		ProductDto p1 = new ProductDto("4k-tv", 1000);
		ProductDto p2 = new ProductDto("slr-camera", 750);
		ProductDto p3 = new ProductDto("iphone", 800);
		ProductDto p4 = new ProductDto("headphone", 100);
		
		Flux.just(p1, p2, p3, p4)
			.concatWith(newProducts())
			.flatMap(pDto -> productService.insertProduct(Mono.just(pDto)))
			.subscribe(System.out::println); // Versión simplificada de la expresión "pDto -> System.out.println(pDto)"
			
	}
	
	private Flux<ProductDto> newProducts() {
		return Flux.range(1, 1000)
				.delayElements(Duration.ofSeconds(2))
				.map(i -> new ProductDto("product-" + i, ThreadLocalRandom.current().nextInt(10, 100)));
	}

}
