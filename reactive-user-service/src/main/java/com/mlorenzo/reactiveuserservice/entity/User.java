package com.mlorenzo.reactiveuserservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

// Nota: Como estamos usando el driver reactivo R2DBC para la base de datos relacional, R2DBC no usa JPA y, por lo tanto, tampoco Hibernate, así que no podemos usar sus anotaciones

@Data
@Table(value = "users")
public class User {
	
	@Id
	private Integer id;
	
	private String name;
	private Integer balance;

}
