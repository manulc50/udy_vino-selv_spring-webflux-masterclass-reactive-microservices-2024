package com.mlorenzo.reactiveorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequestDto {
	private Integer userId;
	private Integer amount;
}
