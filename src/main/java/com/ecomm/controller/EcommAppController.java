package com.ecomm.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.service.EcommAppOrderService;
import com.ecomm.service.EcommAppService;
import com.ecomm.service.EcommCRUDOperationsService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class EcommAppController {

	@Autowired
	private EcommAppService ecommAppService;
	@Autowired
	private EcommAppOrderService ecommAppOrderService;
	@Autowired
	private EcommCRUDOperationsService ecommCRUDOperationsService;
	
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
        return ecommAppService.getAllProductsImpl();
    }
	//get product
	@PostMapping(value = "/getProduct")
    public @NotNull EcommAppResponse getProduct(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.getProductImpl(ecommAppRequest);
    }
	//save product
	@PostMapping(value = "/saveProduct")
    public @NotNull EcommAppResponse saveProducts(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.saveProductImpl(ecommAppRequest);
    }
	//delete product
	@PostMapping(value = "/deleteProduct")
    public @NotNull EcommAppResponse deleteProduct(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.deleteProductImpl(ecommAppRequest);
    }
	//update product
	@PostMapping(value = "/updateProduct")
    public @NotNull EcommAppResponse updateProduct(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.updateProductImpl(ecommAppRequest);
    }
	//save category
	@PostMapping(value = "/saveCategory")
    public @NotNull EcommAppResponse saveCategory(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.saveCategoryImpl(ecommAppRequest);
    }
	//get category
	@PostMapping(value = "/getCategory")
    public @NotNull EcommAppResponse getCategory(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.getCategoryImpl(ecommAppRequest);
    }
	//update category
	@PostMapping(value = "/updateCategory")
    public @NotNull EcommAppResponse updateCategory(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.updateCategoryImpl(ecommAppRequest);
    }
	//delete category
	@PostMapping(value = "/deleteCategory")
    public @NotNull EcommAppResponse deleteCategory(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppService.deleteCategoryImpl(ecommAppRequest);
    }
	//get all categories available
	@GetMapping(value = "/categories")
    public @NotNull EcommAppResponse getCategories() {
        return ecommAppService.getCategoriesImpl();
    }
	//save order
	@PostMapping(value = "/createOrder")
    public @NotNull EcommAppResponse createOrder(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommAppOrderService.createOrderImpl(ecommAppRequest);
    }
	//get all orders available
	@GetMapping(value = "/orders")
    @ResponseStatus(HttpStatus.OK)
    public @NotNull EcommAppResponse list() {
        return this.ecommAppOrderService.getAllOrders();
    }
	@PostMapping("/uploadFile")
    public @NotNull EcommAppResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("productId") String productId) {
		return ecommAppService.uploadFileImpl(file, productId);
	}
	@PostMapping("/fileUpload")
    public @NotNull EcommAppResponse fileUploadBase64(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
		return ecommAppService.fileUploadBase64Impl(ecommAppRequest);
	}
	//crud operations
	@PostMapping(value = "/crudOperations")
    public @NotNull EcommAppResponse crudOperations(@Valid @RequestBody EcommAppRequest ecommAppRequest) {
        return ecommCRUDOperationsService.crudOperationsImpl(ecommAppRequest);
    }
}
