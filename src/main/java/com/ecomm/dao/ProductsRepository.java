package com.ecomm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomm.model.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
