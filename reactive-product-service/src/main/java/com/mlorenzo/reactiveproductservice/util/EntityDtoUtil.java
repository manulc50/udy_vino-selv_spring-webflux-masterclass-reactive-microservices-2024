package com.mlorenzo.reactiveproductservice.util;

import org.springframework.beans.BeanUtils;

import com.mlorenzo.reactiveproductservice.dto.ProductDto;
import com.mlorenzo.reactiveproductservice.entity.Product;

public class EntityDtoUtil {

	public static ProductDto toDto(Product product) {
		ProductDto dto = new ProductDto();
		BeanUtils.copyProperties(product, dto);
		return dto;
	}
	
	public static Product toEntity(ProductDto dto) {
		Product entity = new Product();
		BeanUtils.copyProperties(dto, entity);
		return entity;
	}
}
