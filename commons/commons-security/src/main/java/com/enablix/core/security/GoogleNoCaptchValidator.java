package com.enablix.core.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.enablix.commons.util.json.JsonUtil;

@Component
public class GoogleNoCaptchValidator {
	
	private static final String ENC_UTF_8 = "UTF-8";

	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleNoCaptchValidator.class);

	@Autowired
	private GoogleNoCaptchaProperties properties;
	
	private RestTemplate restTemplate;
	private HttpHeaders headers;

	public GoogleNoCaptchValidator() {
		this.restTemplate = new RestTemplate();
		this.headers = new HttpHeaders();
	    headers.add("Content-Type", "text/plain");
	    headers.add("Accept", "*/*");
	}
	
	@PostConstruct
	public void init() {
		
	}
	
	public boolean validateCaptchaResponse(String response, String remoteIP) {
		
		boolean valid = false;
		
		try {
			
			String postBody = "secret=" + URLEncoder.encode(properties.getServerSecretKey(), ENC_UTF_8) 
							+ "&response=" + URLEncoder.encode(response, ENC_UTF_8)  
							+ "&remoteip=" + URLEncoder.encode(remoteIP, ENC_UTF_8) ;
			
			String validationResponse = post(postBody);
			Map<String, Object> responseMap = JsonUtil.jsonToMap(validationResponse);
			valid = (Boolean) responseMap.get("success");
			
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return valid;
	}
	
	public String post(String postBody) throws UnsupportedEncodingException {   
	    HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
	    String uri = properties.getCaptchaValidationUrl() + "?" + postBody;
	    // TODO: POST method should be used but it is failing to send parameters
	    ResponseEntity<String> responseEntity = restTemplate.exchange(
	    		uri, HttpMethod.GET, requestEntity, String.class);
	    return responseEntity.getBody();
	  }
	
}
