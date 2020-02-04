package com.ecomm.service;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.model.Order;

public interface EcommAppOrderService {

	EcommAppResponse getAllOrders();
	EcommAppResponse createOrderImpl(EcommAppRequest ecommAppRequest);
	void update(Order order);
}
