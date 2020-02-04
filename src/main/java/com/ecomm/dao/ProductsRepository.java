package com.ecomm.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecomm.model.Product;

@Repository
public interface ProductsRepository extends CrudRepository<Product, Integer>{

	@Query("from Product WHERE productId=:productId")
	Product getProductById(@Param("productId") Integer productId);

}
