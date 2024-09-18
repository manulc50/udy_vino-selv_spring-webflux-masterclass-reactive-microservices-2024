package com.mlorenzo.reactiveorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseOrderRequestDto {
	private Integer userId;
	private String productId;
}
