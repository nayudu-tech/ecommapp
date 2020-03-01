package com.ecomm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecomm.model.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer>{

	@Query("from Product WHERE productId=:productId")
	Product getProductById(@Param("productId") Integer productId);

}
