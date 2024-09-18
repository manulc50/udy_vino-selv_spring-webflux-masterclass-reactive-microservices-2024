package com.mlorenzo.reactiveuserservice.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("user_transactions")
public class UserTransaction {

	@Id
	private Integer id;
	
	private Integer userId;
	private Integer amount;
	private LocalDateTime transactionDate;
}
