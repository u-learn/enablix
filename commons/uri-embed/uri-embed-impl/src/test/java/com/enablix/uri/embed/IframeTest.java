package com.enablix.uri.embed;

import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class IframeTest {
	
	public static void main(String[] args) throws URISyntaxException {
		
		RestTemplate restTemplate = new RestTemplate();
		String uri = "https://www.youtube.com";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("User-Agent", "Enablix");
		  
		HttpHeaders response = restTemplate.headForHeaders(uri);
		System.out.println("headers = " + response);
	}

}
