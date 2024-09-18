package com.mlorenzo.reactiveorderservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mlorenzo.reactiveorderservice.client.ProductClient;
import com.mlorenzo.reactiveorderservice.client.UserClient;
import com.mlorenzo.reactiveorderservice.dto.ProductDto;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderRequestDto;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderResponseDto;
import com.mlorenzo.reactiveorderservice.dto.UserDto;
import com.mlorenzo.reactiveorderservice.service.OrderFulfillmentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class ReactiveOrderServiceApplicationTests {
	
	@Autowired
	ProductClient productClient;
	
	@Autowired
	UserClient userClient;
	
	@Autowired
	OrderFulfillmentService orderFulfillmentService;

	@Test
	void contextLoads() {
		Flux<ProductDto> productFlux = productClient.getAllProducts();
		Flux<UserDto> userFlux = userClient.getAllUsers();
		
		Flux<PurchaseOrderResponseDto> purchaseOrderResponseFlux =  Flux.zip(productFlux, userFlux, (p, u) -> new PurchaseOrderRequestDto(u.getId(), p.getId()))
				.flatMap(purchaseOrderRequestDto -> orderFulfillmentService.processOrderApproach2(Mono.just(purchaseOrderRequestDto)))
				.doOnNext(System.out::println); // Versión simplificada de la expresión "purchaseOrderResponseDto -> System.out.println(purchaseOrderResponseDto)"
	
		StepVerifier.create(purchaseOrderResponseFlux)
			.expectNextCount(4)
			.verifyComplete();
	}

}
