package com.mlorenzo.reactiveuserservice.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserTransactionDto {	
	private Integer id;
	private Integer userId;
	private Integer amount;
	private LocalDateTime transactionDate;
}
