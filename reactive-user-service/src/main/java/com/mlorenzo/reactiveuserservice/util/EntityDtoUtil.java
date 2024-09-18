package com.mlorenzo.reactiveuserservice.util;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.mlorenzo.reactiveuserservice.dto.TransactionRequestDto;
import com.mlorenzo.reactiveuserservice.dto.TransactionResponseDto;
import com.mlorenzo.reactiveuserservice.dto.TransactionStatus;
import com.mlorenzo.reactiveuserservice.dto.UserDto;
import com.mlorenzo.reactiveuserservice.dto.UserTransactionDto;
import com.mlorenzo.reactiveuserservice.entity.User;
import com.mlorenzo.reactiveuserservice.entity.UserTransaction;

public class EntityDtoUtil {

	public static UserDto toDto(User user) {
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}
	
	public static User toEntity(UserDto dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		return user;
	}
	
	public static UserTransaction toEntity(TransactionRequestDto requestDto) {
		UserTransaction ut = new UserTransaction();
		ut.setUserId(requestDto.getUserId());
		ut.setAmount(requestDto.getAmount());
		ut.setTransactionDate(LocalDateTime.now());
		return ut;
	}
	
	public static TransactionResponseDto toDto(UserTransaction ut, TransactionStatus status) {
		TransactionResponseDto responseDto = new TransactionResponseDto();
		responseDto.setUserId(ut.getUserId());
		responseDto.setAmount(ut.getAmount());
		responseDto.setStatus(status);
		return responseDto;
	}
	
	public static UserTransactionDto toDto(UserTransaction ut) {
		UserTransactionDto utDto = new UserTransactionDto();
		BeanUtils.copyProperties(ut, utDto);
		return utDto;
	}
}
