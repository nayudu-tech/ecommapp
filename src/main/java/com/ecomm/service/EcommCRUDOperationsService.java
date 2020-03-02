package com.ecomm.service;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;

public interface EcommCRUDOperationsService {

	EcommAppResponse crudOperationsImpl(EcommAppRequest ecommAppRequest);

}
