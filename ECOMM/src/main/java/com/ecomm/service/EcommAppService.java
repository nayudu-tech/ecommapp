package com.ecomm.service;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;

public interface EcommAppService {

	EcommAppResponse registerEcommUserImpl(EcommAppRequest ecommAppRequest);
	EcommAppResponse createAuthenticationTokenImpl(EcommAppRequest authenticationRequest);

}
