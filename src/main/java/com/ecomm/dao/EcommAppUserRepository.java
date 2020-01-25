package com.ecomm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecomm.model.EcommAppUser;

@Repository
public interface EcommAppUserRepository extends JpaRepository<EcommAppUser, Integer>{

	EcommAppUser findByUserEmail(String userEmail);
	
	@Query("from EcommAppUser WHERE userEmail=:userEmail and password=:password")
	EcommAppUser findUser(@Param("userEmail") String userEmail, @Param("password") String password);
}
