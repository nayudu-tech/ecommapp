package com.ecomm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ecomm_products")
public class Product {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "eca_id")
	private Integer productId;
	//@NotNull(message = "Product name is required.")
    //@Basic(optional = false)
	@Column(name = "eca_product_name")
    private String productName;
	@Column(name = "eca_price")
	private Double price;
	@Column(name = "eca_product_img_url")
    private String pictureUrl;
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
}
