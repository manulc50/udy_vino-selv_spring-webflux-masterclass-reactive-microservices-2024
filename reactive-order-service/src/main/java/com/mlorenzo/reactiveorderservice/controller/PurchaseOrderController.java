package com.mlorenzo.reactiveorderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderRequestDto;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderResponseDto;
import com.mlorenzo.reactiveorderservice.service.OrderFulfillmentService;
import com.mlorenzo.reactiveorderservice.service.OrderQueryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class PurchaseOrderController {
	
	@Autowired
	private OrderFulfillmentService orderFulfillmentService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<PurchaseOrderResponseDto>> createOrder(@RequestBody Mono<PurchaseOrderRequestDto> requestDtoMono) {
		return this.orderFulfillmentService.processOrderApproach2(requestDtoMono)
				.map(ResponseEntity::ok) // Versión simplificada de la expresión "purchaseOrderResponseDto -> ResponseEntity.ok(purchaseOrderResponseDto)"
				.onErrorReturn(WebClientResponseException.class, ResponseEntity.badRequest().build())
				.onErrorReturn(WebClientRequestException.class, ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
	}
	
	@GetMapping("user/{userId}")
	public Flux<PurchaseOrderResponseDto> getOrdersByUserId(@PathVariable int userId) {
		return this.orderQueryService.getOrdersByUserId(userId);
	}

}
