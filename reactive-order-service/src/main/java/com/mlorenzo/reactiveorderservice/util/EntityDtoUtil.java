package com.mlorenzo.reactiveorderservice.util;

import org.springframework.beans.BeanUtils;

import com.mlorenzo.reactiveorderservice.dto.OrderStatus;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderDto;
import com.mlorenzo.reactiveorderservice.dto.PurchaseOrderResponseDto;
import com.mlorenzo.reactiveorderservice.dto.TransactionStatus;
import com.mlorenzo.reactiveorderservice.entity.PurchaseOrder;

public class EntityDtoUtil {
	
	public static PurchaseOrder toEntity(PurchaseOrderDto dto) {
		TransactionStatus status = dto.getTransactionResponseDto().getStatus();
		
		PurchaseOrder entity = new PurchaseOrder();
		entity.setProductId(dto.getProductDto().getId());
		entity.setAmount(dto.getProductDto().getPrice());
		entity.setUserId(dto.getTransactionResponseDto().getUserId());
		entity.setStatus(TransactionStatus.APPROVED.equals(status) ? OrderStatus.COMPLETED : OrderStatus.FAILED);
		return entity;
	}
	
	public static PurchaseOrderResponseDto toDto(PurchaseOrder entity) {
		PurchaseOrderResponseDto dto = new PurchaseOrderResponseDto();
		BeanUtils.copyProperties(entity, dto, "id");
		dto.setOrderId(entity.getId());
		return dto;
	}
}
