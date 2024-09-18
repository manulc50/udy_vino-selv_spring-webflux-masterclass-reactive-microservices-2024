package com.mlorenzo.reactiveproductservice.entity;

import org.springframework.data.annotation.Id;


import lombok.Data;

@Data
public class Product {
	
	@Id
	private String id;
	
	private String description;
	private Integer price;
}
