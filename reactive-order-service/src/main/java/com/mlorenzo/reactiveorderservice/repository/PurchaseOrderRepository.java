package com.mlorenzo.reactiveorderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlorenzo.reactiveorderservice.entity.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer>{
	List<PurchaseOrder> findByUserId(Integer userId);
}
