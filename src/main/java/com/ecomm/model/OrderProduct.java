package com.ecomm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ecomm_order_product")
public class OrderProduct implements Serializable{

	
	/*
	 * @GeneratedValue(strategy=GenerationType.IDENTITY)
	 * 
	 * @Column(name = "eca_id") private Integer orderProductId;
	 */
	 
	@EmbeddedId
    @JsonIgnore
    private OrderProductPK pk;
    @Column(name = "eca_quantity", nullable = false)
    private Integer quantity;
    
    public OrderProduct() {
        super();
    }

    public OrderProduct(Order order, Product product, Integer quantity) {
        pk = new OrderProductPK();
        pk.setOrder(order);
        pk.setProduct(product);
        this.quantity = quantity;
    }

	public OrderProductPK getPk() {
		return pk;
	}

	public void setPk(OrderProductPK pk) {
		this.pk = pk;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Transient
    public Product getProduct() {
        return this.pk.getProduct();
    }
 
    @Transient
    public Double getTotalPrice() {
        return getProduct().getPrice() * getQuantity();
    }

	
	/*
	 * public Integer getOrderProductId() { return orderProductId; }
	 * 
	 * public void setOrderProductId(Integer orderProductId) { this.orderProductId =
	 * orderProductId; }
	 */
	 
}
