package com.mlorenzo.reactiveorderservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.mlorenzo.reactiveorderservice.dto.OrderStatus;

import lombok.Data;

// El atributo "name" de la anotación @Entity indica el nombre de la tabla de la base de datos con la que se va a mapear esta clase entidad
// Este atributo "name" es una alternativa equivalente a usar la anotación @Table. En este caso, @Table(name = "purchase_orders")

@Data
@Entity(name = "purchase_orders")
//@Table(name = "purchase_orders")
public class PurchaseOrder {
	
	@Id
	@GeneratedValue // Si no se indica una estrategia, por defecto es AUTO
	private Integer id;
	
	private String productId;
	private Integer userId;
	private Integer amount;
	private OrderStatus status;
}
