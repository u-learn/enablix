package com.enablix.uri.embed.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.uri.embed.service.EmbedException;
import com.enablix.uri.embed.service.EmbedInfoProvider;
import com.enablix.uri.embed.service.IFrameTester;

@Component
public class EmbedRocks implements EmbedInfoProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmbedRocks.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IFrameTester iframeTester;
	
	@Value("${embed.rocks.api.url}")
	private String apiUrl;
	
	@Value("${embed.rocks.api.key}")
	private String apiKey;
	
	@Override
	public EmbedInfo fetchEmbedInfo(String url) {
		
		String lcUrl = url.toLowerCase();
		if (!lcUrl.startsWith("http://")
				&& !lcUrl.startsWith("http://")) {
			url = "http://" + url;
		}
		
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("url", url);
		urlVariables.put("key", apiKey);
		urlVariables.put("skip", "article");
		
		UriTemplate uriTemplate = new UriTemplate(apiUrl);
		URI restUrl = uriTemplate.expand(urlVariables);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Accept", "application/json");
		headers.add("User-Agent", "Enablix");
		  
		RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, restUrl);
		ResponseEntity<EmbedInfo> response = restTemplate.exchange(request, EmbedInfo.class);
		
		if (response.getStatusCode() != HttpStatus.OK) {
			
			LOGGER.error("Error calling embed.rocks: url [{}], params [{}], status [{}]", 
					url, urlVariables, response.getStatusCode());
			
			throw new EmbedException("Error calling embed.rocks");
		}
		
		EmbedInfo embedInfo = response.getBody();
		embedInfo.setIframeEmbeddable(iframeTester.checkIFrameEmbeddable(url));
		
		return embedInfo;
	}
	
	public static void main(String[] args) throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		String uri = "https://api.embed.rocks/api/?url=https://www.youtube.com/watch?v%3DTxA26WxYaA0&key=defbf772-553c-4392-aaf9-7ded5272fcbe&skip=article";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Accept", "application/json");
		headers.add("User-Agent", "Enablix");
		  
		RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, new URI(uri));
		ResponseEntity<EmbedInfo> response = restTemplate.exchange(request, EmbedInfo.class);
		System.out.println("response = " + response.getBody());
	}

}
