package com.ecomm.service;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.model.Product;

public interface EcommAppService {

	EcommAppResponse registerEcommUserImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse createAuthenticationTokenImpl(EcommAppRequest authenticationRequest);
	EcommAppResponse getAllProducts();
	Product getProduct(int id);
	EcommAppResponse saveProduct(EcommAppRequest ecommAppRequest);
	EcommAppResponse saveCategory(EcommAppRequest ecommAppRequest);
	EcommAppResponse getCategories();
}
