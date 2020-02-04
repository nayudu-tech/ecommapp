package com.ecomm.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.service.EcommAppOrderService;
import com.ecomm.service.EcommAppService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class EcommAppController {

	@Autowired
	private EcommAppService ecommAppService;
	@Autowired
	private EcommAppOrderService ecommAppOrderService;
	
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
	//save category
	@PostMapping(value = "/saveCategory")
    public @NotNull EcommAppResponse saveCategory(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.saveCategory(ecommAppRequest);
    }
	//get all categories available
	@GetMapping(value = "/categories")
    public @NotNull EcommAppResponse getCategories() {
        return ecommAppService.getCategories();
    }
	//save order
	@PostMapping(value = "/createOrder")
    public @NotNull EcommAppResponse createOrder(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppOrderService.createOrderImpl(ecommAppRequest);
    }
	@GetMapping(value = "/orders")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull EcommAppResponse list() {
        return this.ecommAppOrderService.getAllOrders();
    }
}
