package com.mlorenzo.reactiveorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseOrderDto {
	private ProductDto productDto;
	private TransactionResponseDto transactionResponseDto;
}
