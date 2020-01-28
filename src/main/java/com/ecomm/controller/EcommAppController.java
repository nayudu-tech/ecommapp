package com.ecomm.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.model.Product;
import com.ecomm.service.EcommAppService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class EcommAppController {

	@Autowired
	private EcommAppService ecommAppService;
	
	//register ecomm app user
	@PostMapping(value = "/register")
	public EcommAppResponse registerEcommUser(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
		return ecommAppService.registerEcommUserImpl(ecommAppRequest);
	}
	//authenticate ecomm user using email and password, returns jwt token in response
	@PostMapping(value = "/authenticate")
	public EcommAppResponse createAuthenticationToken(@Valid @RequestBody EcommAppRequest authenticationRequest) {
		return ecommAppService.createAuthenticationTokenImpl(authenticationRequest);
	}
	//get all products available
	@GetMapping(value = "/products")
    public @NotNull EcommAppResponse getProducts() {
        return ecommAppService.getAllProducts();
    }
	
	//save product
	@PostMapping(value = "/saveProduct")
    public @NotNull EcommAppResponse saveProducts(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.saveProduct(ecommAppRequest);
    }
}
