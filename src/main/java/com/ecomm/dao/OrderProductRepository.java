package com.ecomm.dao;

import org.springframework.data.repository.CrudRepository;

import com.ecomm.model.OrderProduct;
import com.ecomm.model.OrderProductPK;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK>{

}
