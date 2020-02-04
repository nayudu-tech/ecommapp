package com.ecomm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import com.ecomm.dto.EcommAppRequest;
import com.ecomm.dto.EcommAppResponse;

public class Utility {

	private static final Utility utility = new Utility();
	private Utility() {}
	public static Utility getInstance() {
		return utility;
	}
	
	public EcommAppResponse successResponse(EcommAppRequest ecommAppRequest, EcommAppResponse ecommAppResponse, String message) {
		ecommAppResponse.setStatus("success");
		ecommAppResponse.setStatusCode(200);
		ecommAppResponse.setMessage(message);
		
		return ecommAppResponse;
	}
	
	public EcommAppResponse failureResponse(EcommAppRequest ecommAppRequest, EcommAppResponse ecommAppResponse, String message) {
		ecommAppResponse.setStatus("failure");
		ecommAppResponse.setStatusCode(300);
		ecommAppResponse.setMessage(message);
		
		return ecommAppResponse;
	}
	
	public String readProperty(String key) {
		String value = "";
		
		try (InputStream input = Utility.class.getClassLoader().getResourceAsStream("appmessages.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value
            value = prop.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return value;
	}
	
	public static Long generateRandom(int length) {
	    Random random = new Random();
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    return Long.parseLong(new String(digits));
	}
}
