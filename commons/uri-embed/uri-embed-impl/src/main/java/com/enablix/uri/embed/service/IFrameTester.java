package com.enablix.uri.embed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.enablix.commons.util.StringUtil;

@Component
public class IFrameTester {

	@Autowired
	protected RestTemplate restTemplate;
	
	public boolean checkIFrameEmbeddable(String uri) {

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("User-Agent", "Enablix");
		  
		try {
			
			HttpHeaders response = restTemplate.headForHeaders(uri);
			String xFrameOptions = response.getFirst("X-Frame-Options");
			return !StringUtil.hasText(xFrameOptions);
			
		} catch (HttpClientErrorException e) {
			return true;
		}
	}
	
}
