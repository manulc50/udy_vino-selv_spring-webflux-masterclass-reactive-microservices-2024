package com.mlorenzo.reactiveorderservice.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mlorenzo.reactiveorderservice.client.ProductClient;
import com.mlorenzo.reactiveorderservice.client.UserClient;
import com.mlorenzo.reactiveorderservice.dto.ProductDto;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderDto;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderRequestDto;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderResponseDto;
import com.mlorenzo.reactiveorderservice.dto.TransactionRequestDto;
import com.mlorenzo.reactiveorderservice.dto.TransactionResponseDto;
import com.mlorenzo.reactiveorderservice.repository.PurchaseOrderRepository;
import com.mlorenzo.reactiveorderservice.util.EntityDtoUtil;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Service
public class OrderFulfillmentService {
	
	@Autowired
	private ProductClient productClient;
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	public Mono<PurchaseOrderResponseDto> processOrderApproach1(Mono<PurchaseOrderRequestDto> requestDtoMono) {	
		Mono<PurchaseOrderDto> purchaseOrderDtoMono = requestDtoMono
				.flatMap(requestDto -> productRequestResponse(requestDto)
						.flatMap(productDto -> userRequestResponse(new TransactionRequestDto(requestDto.getUserId(),productDto.getPrice()))
								.map(tranResponseDto -> new PurchaseOrderDto(productDto, tranResponseDto))));

		return purchaseOrderDtoMono
				.map(EntityDtoUtil::toEntity) //  Versión simplificada de la expresión "purchaseOrderDto -> EntityDtoUtil.toEntity(purchaseOrderDto)"
				// Blocking driver(No reactive)
				.map(purchaseOrderRepository::save) //  Versión simplificada de la expresión "entity -> purchaseOrderRepository.save(entity)"
				.map(EntityDtoUtil::toDto) // Versión simplificada de la expresión "entity -> EntityDtoUtil.toDto(entity)"
				// Como no se está usando un driver reactivo en la base de datos, la operación de salvado anterior es un proceso síncrono y bloqueante y, para evitar que afecte al rendimiento de la aplicación, es recomendable usar la siguiente técnica: 
				// When there is a need to use a source of information that is synchronous and blocking, the recommended pattern to use in Reactor is as follows:
				// By using the Schedulers.boundedElastic() we ensure that each subscription happens on a dedicated single-threaded worker, not impacting other non-blocking processing
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	public Mono<PurchaseOrderResponseDto> processOrderApproach2(Mono<PurchaseOrderRequestDto> requestDtoMono) {
		Mono<PurchaseOrderDto> purchaseOrderDtoMono = requestDtoMono
			.flatMap(requestDto -> productRequestResponse(requestDto)
				.zipWhen(productDto -> userRequestResponse(new TransactionRequestDto(requestDto.getUserId(), productDto.getPrice())),
						(productDto, tranResponseDto) -> new PurchaseOrderDto(productDto, tranResponseDto)));
		
		return purchaseOrderDtoMono
				.map(EntityDtoUtil::toEntity) //  Versión simplificada de la expresión "purchaseOrderDto -> EntityDtoUtil.toEntity(purchaseOrderDto)"
				// Blocking driver(No reactive)
				.map(purchaseOrderRepository::save) //  Versión simplificada de la expresión "entity -> purchaseOrderRepository.save(entity)"
				.map(EntityDtoUtil::toDto) // Versión simplificada de la expresión "entity -> EntityDtoUtil.toDto(entity)"
				// Como no se está usando un driver reactivo en la base de datos, la operación de salvado anterior es un proceso síncrono y bloqueante y, para evitar que afecte al rendimiento de la aplicación, es recomendable usar la siguiente técnica: 
				// When there is a need to use a source of information that is synchronous and blocking, the recommended pattern to use in Reactor is as follows:
				// By using the Schedulers.boundedElastic() we ensure that each subscription happens on a dedicated single-threaded worker, not impacting other non-blocking processing
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	
	private Mono<ProductDto> productRequestResponse(PurchaseOrderRequestDto requestDto) {
		return productClient.getProductById(requestDto.getProductId())
				// En caso de fallo con la comunicación con el servicio de productos, configuramos 5 reintentos que se realizarán cada segundo
				.retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)));
	}
	
	private Mono<TransactionResponseDto> userRequestResponse(TransactionRequestDto requestDto) {
		return userClient.authorizeTransaction(requestDto);
	}

}
