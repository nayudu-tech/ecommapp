package com.ecomm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecomm.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	@Query("from Category WHERE categoryName=:categoryName")
	Category getCategoryByName(@Param("categoryName") String categoryName);
}
