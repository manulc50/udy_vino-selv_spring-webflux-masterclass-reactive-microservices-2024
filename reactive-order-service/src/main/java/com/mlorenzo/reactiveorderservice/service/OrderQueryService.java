package com.mlorenzo.reactiveorderservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderResponseDto;
import com.mlorenzo.reactiveorderservice.repository.PurchaseOrderRepository;
import com.mlorenzo.reactiveorderservice.util.EntityDtoUtil;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderQueryService {
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	public Flux<PurchaseOrderResponseDto> getOrdersByUserId(int userId) {
		/*
		// Esta manera no es reactiva del todo porque estamos usando esta operación síncrona y bloqueante fuera del pipeline reactivo
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByUserId(userId);
		
		// Aquí empieza el pipeline reactivo y, como se puede ver, la sentencia de arriba, que es síncrona y bloqueante, está fuera de este pipeline reactivo
		return Flux.fromIterable(purchaseOrders)
				.map(EntityDtoUtil::toDto); // Versión simplficada de la expresión "purchaseOrder -> EntityDtoUtil.toDto(purchaseOrder)"
		*/
		
		// Esta otra manera es mejor porque toda la lógica se produce dentro del pipeline reactivo
		return Flux.fromStream(() -> purchaseOrderRepository.findByUserId(userId).stream()) // // Blocking driver(No reactive)
				.map(EntityDtoUtil::toDto) // Versión simplficada de la expresión "purchaseOrder -> EntityDtoUtil.toDto(purchaseOrder)"
				// Como no se está usando un driver reactivo en la base de datos, la operación de salvado anterior es un proceso síncrono y bloqueante y, para evitar que afecte al rendimiento de la aplicación, es recomendable usar la siguiente técnica: 
				// When there is a need to use a source of information that is synchronous and blocking, the recommended pattern to use in Reactor is as follows:
				// By using the Schedulers.boundedElastic() we ensure that each subscription happens on a dedicated single-threaded worker, not impacting other non-blocking processing
				.subscribeOn(Schedulers.boundedElastic());
	}

}
