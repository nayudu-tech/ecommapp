package com.ecomm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomm.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

}
