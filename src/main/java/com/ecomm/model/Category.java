package com.ecomm.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ecomm_categories")
public class Category {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "eca_id")
	private Integer categoryId;
	@Column(name = "eca_category_name")
    private String categoryName;
	@OneToMany(targetEntity=Product.class, mappedBy = "category", fetch = FetchType.LAZY)
	private List<Product> products;
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
