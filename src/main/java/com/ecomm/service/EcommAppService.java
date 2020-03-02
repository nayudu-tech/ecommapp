package com.ecomm.service;

import org.springframework.web.multipart.MultipartFile;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;

public interface EcommAppService {

	EcommAppResponse registerEcommUserImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse createAuthenticationTokenImpl(EcommAppRequest authenticationRequest);
	EcommAppResponse getAllProductsImpl();
	EcommAppResponse saveProductImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse saveCategoryImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse getCategoriesImpl();
	EcommAppResponse uploadFileImpl(MultipartFile file, String productId);
	EcommAppResponse deleteProductImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse updateProductImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse updateCategoryImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse deleteCategoryImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse getProductImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse getCategoryImpl(EcommAppRequest ecommAppRequest);
}
