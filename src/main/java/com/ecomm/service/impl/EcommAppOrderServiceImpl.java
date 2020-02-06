package com.ecomm.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ecomm.dao.OrderProductRepository;
import com.ecomm.dao.OrderRepository;
import com.ecomm.dao.ProductsRepository;
import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;
import com.ecomm.dto.OrderProductDto;
import com.ecomm.model.Order;
import com.ecomm.model.OrderProduct;
import com.ecomm.model.Product;
import com.ecomm.service.EcommAppOrderService;
import com.ecomm.util.Utility;

@Service
@SuppressWarnings("all")
public class EcommAppOrderServiceImpl implements EcommAppOrderService{

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductsRepository productsRepository;
	@Autowired
	private OrderProductRepository orderProductRepository;
	private static final Logger logger = LoggerFactory.getLogger(EcommAppOrderServiceImpl.class);
	
	@Override
	public EcommAppResponse getAllOrders() {
		logger.debug("inside method getAllOrders");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		try {
			Iterable<Order> orders = orderRepository.findAll();
			if(orders != null) {
				ecommResponse.setOrders(orders);
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
    public EcommAppResponse createOrderImpl(EcommAppRequest ecommAppRequest) {
		logger.debug("inside method createOrderImpl");
		EcommAppResponse ecommResponse = new EcommAppResponse();
		
		try {
			validateProductsExistence(ecommAppRequest.getOrderProductDtos());
			Order order = new Order();
			order = savingOrderObject(ecommAppRequest, order);
	        List<OrderProduct> orderProducts = new ArrayList<>();
	        for (OrderProductDto dto : ecommAppRequest.getOrderProductDtos()) {
	        	Product product = productsRepository.getProductById(dto.getProduct().getProductId());
	            orderProducts.add(orderProductRepository.save(new OrderProduct(order, product, dto.getQuantity())));
	        }
	        order.setOrderProducts(orderProducts);
	        //order.setOrderAmount(order.getTotalOrderPrice());
	        this.orderRepository.save(order);
	        ecommResponse.setOrder(order);
	        return Utility.getInstance().successResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("transaction.successful"));
		}catch(Exception e) {
			logger.error("technical error message ::"+e.getMessage());
			return Utility.getInstance().failureResponse(new EcommAppRequest(), ecommResponse, Utility.getInstance().readProperty("technical.error.msg"));
		}
    }
	
	private void validateProductsExistence(List<OrderProductDto> orderProducts) {
        List<OrderProductDto> list = orderProducts
          .stream()
          .filter(op -> Objects.isNull(productsRepository.findById(op
            .getProduct()
            .getProductId())))
          .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) {
            new ResourceNotFoundException("Product not found");
        }
    }
	
	private Order savingOrderObject(EcommAppRequest ecommAppRequest, Order order) {
		
		Date date = new Date();
		order.setOrderDate(new Timestamp(date.getTime()));
        order.setStatus("PAID");
        order.setOrderNo("OID-"+Utility.getInstance().generateRandom(12));
        order.setCustomerName(ecommAppRequest.getCustomerName());
        order.setCustomerAddress(ecommAppRequest.getCustomerAddress());
        order.setCustomerPhone(ecommAppRequest.getCustomerPhone());
        order.setCustomerEmail(ecommAppRequest.getCustomerEmail());
        order = this.orderRepository.save(order);
        
        return order;
	}
	
	@Override
    public void update(Order order) {
        this.orderRepository.save(order);
    }
}
