package com.ecomm.dto;

import com.ecomm.model.Product;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("all")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class EcommAppResponse {

	private String token;
	private String email;
	private String userName;
	private String activationDate;
	private String lastLoginDate;
	private String mobileNo;
	private String status;
	private Integer statusCode;
	private String message;
	private Iterable<Product> products;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Iterable<Product> getProducts() {
		return products;
	}
	public void setProducts(Iterable<Product> products) {
		this.products = products;
	}
}
