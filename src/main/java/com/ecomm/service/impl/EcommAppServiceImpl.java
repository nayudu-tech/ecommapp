package com.ecomm.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ecomm.dao.CategoryRepository;
import com.ecomm.dao.EcommAppUserRepository;
import com.ecomm.dao.ProductsRepository;
import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.jwt.config.JwtTokenUtil;
import com.ecomm.model.Category;
import com.ecomm.model.EcommAppUser;
import com.ecomm.model.Product;
import com.ecomm.service.EcommAppService;
import com.ecomm.util.Utility;

@Service
public class EcommAppServiceImpl implements EcommAppService, UserDetailsService {
	
	@Autowired
	private EcommAppUserRepository ecommAppUserRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private PasswordEncoder bcryptEncoder;
	@Autowired
	private ProductsRepository productsRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	private static final Logger logger = LoggerFactory.getLogger(EcommAppServiceImpl.class);

	@Override
	public EcommAppResponse registerEcommUserImpl(EcommAppRequest ecommAppRequest) {
		logger.info("inside method registerUserImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			if(ecommAppRequest.getEmail() != null && !ecommAppRequest.getEmail().isEmpty()) {
				if(ecommAppUserRepository.findByUserEmail(ecommAppRequest.getEmail()) == null) {
					EcommAppUser ecommAppNewUser = saveEcommAppUser(ecommAppRequest);
					if(ecommAppNewUser != null) {
						logger.info("Registration successful with email :: "+ecommAppRequest.getEmail());
						ecommResponse.setEmail(ecommAppRequest.getEmail());
						ecommResponse.setUserName(ecommAppNewUser.getFirstName() + " " + ecommAppNewUser.getLastName());
						return Utility.getInstance().successResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("user.registration.success.msg"));
					}else {
						logger.debug("Registration failed with email :: "+ecommAppRequest.getEmail());
						return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("user.registration.failed.msg"));
					}
				}else {
					logger.debug("Already register user :: "+ecommAppRequest.getEmail());
					return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("user.registration.failed.msg1"));
				}
			}else {
				logger.debug("Username is mandatory ");
				return Utility.getInstance().failureResponse(ecommAppRequest, ecommResponse, Utility.getInstance().readProperty("user.login.failed.msg3"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}

	@Override
	public EcommAppResponse createAuthenticationTokenImpl(EcommAppRequest authenticationRequest) {
		logger.debug("inside method createAuthenticationTokenImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
			final UserDetails userDetails = loadUserByUsername(authenticationRequest.getEmail());
			final String token = jwtTokenUtil.generateToken(userDetails);
			if(token != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a z"); 
				logger.debug("User Successfully loggedin");
				EcommAppUser userData = ecommAppUserRepository.findByUserEmail(authenticationRequest.getEmail());
				ecommResponse.setToken(token);
				ecommResponse.setEmail(authenticationRequest.getEmail());
				ecommResponse.setUserName(userData.getFirstName() + " " + userData.getLastName());
				if(userData.getMobileNo() != null)
					ecommResponse.setMobileNo(userData.getMobileNo());
				if(userData.getLastLoginDate() != null)
					ecommResponse.setLastLoginDate(formatter.format(userData.getLastLoginDate()));
				Timestamp loginDate = new Timestamp(System.currentTimeMillis());
				userData.setLastLoginDate(loginDate);
				ecommAppUserRepository.save(userData);
				return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("user.login.success.msg"));
			}else {
				logger.debug("Login failed, please try again");
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("user.login.failed.msg"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			if(e.getMessage().equalsIgnoreCase("INVALID_CREDENTIALS")) {
				logger.error("invalid credentials");
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("user.login.failed.msg1"));
			}else if(e.getMessage().equalsIgnoreCase("USER_DISABLED")){
				logger.error("user disabled");
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("user.login.failed.msg2"));
			}else {
				logger.error("other exception");
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
			}
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EcommAppUser userDetails = ecommAppUserRepository.findByUserEmail(username);
		if (userDetails == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(userDetails.getUserEmail(), userDetails.getPassword(), new ArrayList<>());
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	private EcommAppUser saveEcommAppUser(EcommAppRequest ecommAppRequest) {
		logger.info("inside method saveEcommAppUser");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		EcommAppUser ecommAppNewUser = new EcommAppUser();
		ecommAppNewUser.setUserEmail(ecommAppRequest.getEmail());
		if(ecommAppRequest.getPassword() != null && !ecommAppRequest.getPassword().isEmpty())
			ecommAppNewUser.setPassword(bcryptEncoder.encode(ecommAppRequest.getPassword()));
		if(ecommAppRequest.getFirstName() != null && !ecommAppRequest.getFirstName().isEmpty())
			ecommAppNewUser.setFirstName(ecommAppRequest.getFirstName());
		if(ecommAppRequest.getLastName() != null && !ecommAppRequest.getLastName().isEmpty())
			ecommAppNewUser.setLastName(ecommAppRequest.getLastName());
		if(ecommAppRequest.getMobileNo() != null && !ecommAppRequest.getMobileNo().isEmpty())
			ecommAppNewUser.setMobileNo(ecommAppRequest.getMobileNo());
		ecommAppNewUser.setActivationDate(timestamp);
		
		EcommAppUser ecommAppSavedUser = ecommAppUserRepository.save(ecommAppNewUser);
		
		return ecommAppSavedUser;
	}

	@Override
	public EcommAppResponse getAllProductsImpl() {
		logger.debug("inside method getAllProducts");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			Iterable<Product> products = productsRepository.findAll();
			if(products != null) {
				ecommResponse.setProducts(products);
				return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
			}else {
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("no.data.found"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}
	
    public Product getProduct(int id) {
        return productsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
	
    public Category getCategory(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
	
	@Override
    public EcommAppResponse saveProductImpl(EcommAppRequest ecommAppRequest) {
		logger.debug("inside method saveProduct");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			Product savedProduct = productsRepository.save(ecommAppRequest.getProduct());
			if(savedProduct != null) {
				ecommResponse.setProductId(savedProduct.getProductId().toString());
				return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.save.success.msg"));
			}else {
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.save.failed.msg"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
    }
	
	@Override
    public EcommAppResponse saveCategoryImpl(EcommAppRequest ecommAppRequest) {
		logger.debug("inside method saveCategory");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
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
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}

	@Override
	public EcommAppResponse getCategoriesImpl() {
		logger.debug("inside method getCategories");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			Iterable<Category> categories = categoryRepository.findAll();
			if(categories != null) {
				ecommResponse.setCategories(categories);
				return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
			}else {
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("no.data.found"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}

	@Override
	public EcommAppResponse uploadFileImpl(MultipartFile file, String productId) {
		EcommAppResponse ecommResponse = storeFile(file, productId);
		String idType = "";
		try {
			
			idType = String.valueOf(productId.charAt(0));
			
			if(ecommResponse.getStatusCode() == 200) {
				if(!idType.equals("") && idType.equalsIgnoreCase("P")) {
					Product product = getProduct(Integer.parseInt(productId.substring(0, productId.length() - 1)));
					if(product != null) {
						String fileDownloadUri = "http://ec2-18-222-3-132.us-east-2.compute.amazonaws.com:8080/products-imgs/"+ecommResponse.getFileName();
						product.setPictureUrl(fileDownloadUri);
						productsRepository.save(product);
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
					}
				}else if(!idType.equals("") && idType.equalsIgnoreCase("C")) {
					Category category = getCategory(Integer.parseInt(productId.substring(0, productId.length() - 1)));
					if(category != null) {
						String fileDownloadUri = "http://ec2-18-222-3-132.us-east-2.compute.amazonaws.com:8080/category-imgs/"+ecommResponse.getFileName();
						category.setCategoryImgUrl(fileDownloadUri);
						categoryRepository.save(category);
						return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
					}
				}
			}
			
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
		return ecommResponse;
	}
	
	private EcommAppResponse storeFile(MultipartFile file, String productId) {
		
		EcommAppResponse ecommAppResponse = new EcommAppResponse();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path fileStorageLocation = null;

        try {
        	String idType = String.valueOf(productId.charAt(0));
        	if(!idType.equals("") && idType.equalsIgnoreCase("P")) {
        		fileStorageLocation = Paths.get(Utility.getInstance().readProperty("file.upload-product-dir")).toAbsolutePath().normalize();
        	}else if(!idType.equals("") && idType.equalsIgnoreCase("C")) {
        		fileStorageLocation = Paths.get(Utility.getInstance().readProperty("file.upload-category-dir")).toAbsolutePath().normalize();
        	}
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
            	return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommAppResponse, "Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            ecommAppResponse.setFileName(fileName);
            return Utility.getInstance().successResponse(new EcommAppRequest(), ecommAppResponse, Utility.getInstance().readProperty("transaction.successful"));
        } catch (IOException ex) {
        	return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommAppResponse, "Could not store file " + fileName + ". Please try again!");
        }
    }

	@Override
	public EcommAppResponse deleteProductImpl(EcommAppRequest ecommAppRequest) {
		logger.info("inside method deleteProductImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
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
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}

	@Override
	public EcommAppResponse updateProductImpl(EcommAppRequest ecommAppRequest) {
		logger.info("inside method updateProductImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			if(ecommAppRequest.getProduct().getProductId() != null && !ecommAppRequest.getProduct().getProductId().equals("")) {
				Product product = getProduct(ecommAppRequest.getProduct().getProductId());
				if(product != null) {
					logger.info("Successfully Updated the Product");
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
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}
	
	@Override
	public EcommAppResponse updateCategoryImpl(EcommAppRequest ecommAppRequest) {
		logger.info("inside method updateProductImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			if(ecommAppRequest.getCategory().getCategoryId() != null && !ecommAppRequest.getCategory().getCategoryId().equals("")) {
				Category category = getCategory(ecommAppRequest.getCategory().getCategoryId());
				if(category != null) {
					logger.info("Successfully Updated the Category");
					category = categoryRepository.save(category);
					ecommResponse.setProductId(category.getCategoryId().toString());
					return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.update.success.msg"));
				}else {
					logger.info("Failed to Update the Category");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.not.found.msg"));
				}
			}else {
				logger.info("Category Id is mandatory");
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.id.mandatory"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}
	
	@Override
	public EcommAppResponse deleteCategoryImpl(EcommAppRequest ecommAppRequest) {
		logger.info("inside method deleteCategoryImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
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
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}

	@Override
	public EcommAppResponse getProductImpl(EcommAppRequest ecommAppRequest) {
		logger.info("inside method getProductImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			if(ecommAppRequest.getProduct().getProductId() != null && !ecommAppRequest.getProduct().getProductId().equals("")) {
				Product product = getProduct(ecommAppRequest.getProduct().getProductId());
				if(product != null) {
					ecommResponse.setProduct(product);
					return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.update.success.msg"));
				}else {
					logger.info("Failed to fetch the Product");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.not.found.msg"));
				}
			}else {
				logger.info("Product Id is mandatory");
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("product.id.mandatory"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}

	@Override
	public EcommAppResponse getCategoryImpl(EcommAppRequest ecommAppRequest) {
		logger.info("inside method getCategoryImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			if(ecommAppRequest.getCategory().getCategoryId() != null && !ecommAppRequest.getCategory().getCategoryId().equals("")) {
				Category category = getCategory(ecommAppRequest.getCategory().getCategoryId());
				if(category != null) {
					logger.info("Successfully Updated the Category");
					ecommResponse.setCategory(category);
					return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.update.success.msg"));
				}else {
					logger.info("Failed to Update the Category");
					return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.not.found.msg"));
				}
			}else {
				logger.info("Category Id is mandatory");
				return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("category.id.mandatory"));
			}
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
	}
}
