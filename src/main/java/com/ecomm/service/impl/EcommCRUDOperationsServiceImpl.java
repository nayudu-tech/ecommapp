package com.ecomm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.ecomm.dao.CategoryRepository;
import com.ecomm.dao.ProductsRepository;
import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.model.Category;
import com.ecomm.model.Product;
import com.ecomm.service.EcommCRUDOperationsService;
import com.ecomm.util.Utility;

@Service
public class EcommCRUDOperationsServiceImpl implements EcommCRUDOperationsService{
	
	@Autowired
	private ProductsRepository productsRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	private static final Logger logger = LoggerFactory.getLogger(EcommAppServiceImpl.class);

	@Override
	public EcommAppResponse crudOperationsImpl(EcommAppRequest ecommAppRequest) {
		EcommAppResponse ecommResponse = new EcommAppResponse();
		String serviceName = "";
		String operationType = "";
		
		try {
			
			if(ecommAppRequest.getOperationType() != null && !ecommAppRequest.getOperationType().isEmpty())
				operationType = ecommAppRequest.getOperationType();
			if(ecommAppRequest.getModuleName() != null && !ecommAppRequest.getModuleName().isEmpty())
				serviceName = ecommAppRequest.getModuleName();
			
			switch (serviceName) {
			case "Products":
				ecommResponse = productsCRUDOperations(ecommAppRequest, operationType, ecommResponse);
				break;
			case "Categories":
				ecommResponse = categoriesCRUDOperations(ecommAppRequest, operationType, ecommResponse);
				break;
			default:
				logger.debug("Invalid module name");
				return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("crud.operations.failed.msg2"));
			}
			
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
		return ecommResponse;
	}

	private EcommAppResponse productsCRUDOperations(EcommAppRequest ecommAppRequest, String operationType, EcommAppResponse ecommResponse) {
		logger.info("method productsCRUDOperations starts");
		if(!operationType.equals("")) {
			if(operationType.equals("FindAll")) {
				logger.debug("inside method getAllProducts");
				Iterable<Product> products = productsRepository.findAll();
				if(products != null) {
					ecommResponse.setProducts(products);
					return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
				}else {
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("no.data.found"));
				}
			}else if(operationType.equals("Save")) {
				Product savedProduct = productsRepository.save(ecommAppRequest.getProduct());
				if(savedProduct != null) {
					ecommResponse.setProductId(savedProduct.getProductId().toString());
					return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.save.success.msg"));
				}else {
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.save.failed.msg"));
				}
			}else if(operationType.equals("Update")) {
				if(ecommAppRequest.getProduct().getProductId() != null && !ecommAppRequest.getProduct().getProductId().equals("")) {
					Product product = getProduct(ecommAppRequest.getProduct().getProductId());
					if(product != null) {
						logger.info("Successfully Updated the Product");
						if(ecommAppRequest.getProduct().getPrice() != null && !ecommAppRequest.getProduct().getPrice().equals(""))
							product.setPrice(ecommAppRequest.getProduct().getPrice());
						if(ecommAppRequest.getProduct().getProductMinimumOrder() != null && !ecommAppRequest.getProduct().getProductMinimumOrder().equals(""))
							product.setProductMinimumOrder(ecommAppRequest.getProduct().getProductMinimumOrder());
						if(ecommAppRequest.getProduct().getProductName() != null && !ecommAppRequest.getProduct().getProductName().equals(""))
							product.setProductName(ecommAppRequest.getProduct().getProductName());
						product = productsRepository.save(product);
						ecommResponse.setProductId(product.getProductId().toString());
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.update.success.msg"));
					}else {
						logger.info("Failed to Update the Product");
						return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.not.found.msg"));
					}
				}else {
					logger.info("Product Id is mandatory");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.id.mandatory"));
				}
			}else if(operationType.equals("FindById")) {
				if(ecommAppRequest.getProductId() != null && !ecommAppRequest.getProductId().equals("")) {
					Product product = getProduct(Integer.parseInt(ecommAppRequest.getProductId()));
					if(product != null) {
						ecommResponse.setProduct(product);
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
					}else {
						logger.info("Failed to fetch the Product");
						return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.not.found.msg"));
					}
				}else {
					logger.info("Product Id is mandatory");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.id.mandatory"));
				}
			}else if(operationType.equals("Delete")) {
				if(ecommAppRequest.getProductId() != null && !ecommAppRequest.getProductId().equals("")) {
					Product product = getProduct(Integer.parseInt(ecommAppRequest.getProductId()));
					if(product != null) {
						logger.info("Successfully Deleted the Product");
						productsRepository.delete(product);
						ecommResponse.setProductId(product.getProductId().toString());
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.delete.success.msg"));
					}else {
						logger.info("Failed to Delete the Product");
						return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.not.found.msg"));
					}
				}else {
					logger.info("Product Id is mandatory");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.id.mandatory"));
				}
			}else {
				logger.debug("Invalid Operation Type");
				return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("crud.operations.failed.msg1"));
			}
		}else {
			logger.debug("Operation Type is mandatory");
			return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("crud.operations.failed.msg"));
		}
	}
	
	private EcommAppResponse categoriesCRUDOperations(EcommAppRequest ecommAppRequest, String operationType, EcommAppResponse ecommResponse) {
		logger.info("method categoriesCRUDOperations starts");
		if(!operationType.equals("")) {
			if(operationType.equals("FindAll")) {
				Iterable<Category> categories = categoryRepository.findAll();
				if(categories != null) {
					ecommResponse.setCategories(categories);
					return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
				}else {
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("no.data.found"));
				}
			}else if(operationType.equals("Save")) {
				Category categoryByName = categoryRepository.getCategoryByName(ecommAppRequest.getCategory().getCategoryName());
				if(categoryByName != null) {
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.save.failed.msg"));
				}
				Category category = categoryRepository.save(ecommAppRequest.getCategory());
				if(category != null) {
					ecommResponse.setCategoryId(category.getCategoryId().toString());
					return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.save.success.msg"));
				}else {
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.save.failed.msg"));
				}
			}else if(operationType.equals("Update")) {
				if(ecommAppRequest.getCategory().getCategoryId() != null && !ecommAppRequest.getCategory().getCategoryId().equals("")) {
					
					Category category = getCategory(ecommAppRequest.getCategory().getCategoryId());
					if(category != null) {
						logger.info("Successfully Updated the Category");
						if(ecommAppRequest.getCategory().getCategoryName() != null && !ecommAppRequest.getCategory().getCategoryName().equals(""))
							category.setCategoryName(ecommAppRequest.getCategory().getCategoryName());
						category = categoryRepository.save(category);
						ecommResponse.setCategoryId(category.getCategoryId().toString());
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.update.success.msg"));
					}else {
						logger.info("Failed to Update the Category");
						return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.not.found.msg"));
					}
				}else {
					logger.info("Category Id is mandatory");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.id.mandatory"));
				}
			}else if(operationType.equals("FindById")) {
				if(ecommAppRequest.getCategoryId() != null && !ecommAppRequest.getCategoryId().equals("")) {
					Category category = getCategory(Integer.parseInt(ecommAppRequest.getCategoryId()));
					if(category != null) {
						logger.info("Successfully Updated the Category");
						ecommResponse.setCategory(category);
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
					}else {
						logger.info("Failed to Update the Category");
						return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.not.found.msg"));
					}
				}else {
					logger.info("Category Id is mandatory");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.id.mandatory"));
				}
			}else if(operationType.equals("Delete")) {
				if(ecommAppRequest.getCategoryId() != null && !ecommAppRequest.getCategoryId().equals("")) {
					Category category = getCategory(Integer.parseInt(ecommAppRequest.getCategoryId()));
					if(category != null) {
						logger.info("Successfully Deleted the Category");
						categoryRepository.delete(category);
						ecommResponse.setCategoryId(category.getCategoryId().toString());
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.delete.success.msg"));
					}else {
						logger.info("Failed to Delete the Category");
						return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.not.found.msg"));
					}
				}else {
					logger.info("Product Id is mandatory");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.id.mandatory"));
				}
			}else {
				logger.debug("Invalid Operation Type");
				return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("crud.operations.failed.msg1"));
			}
		}else {
			logger.debug("Operation Type is mandatory");
			return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("crud.operations.failed.msg"));
		}
	}
	
	public Product getProduct(int id) {
        return productsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
	
	public Category getCategory(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
