package com.enablix.dms.sharepoint.online;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.enablix.commons.util.json.JsonUtil;
import com.enablix.dms.sharepoint.SharepointUtil;
import com.google.common.base.Joiner;

@Component
public class SharepointOnlineSessionBuilder {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${sharepoint.online.landing.page.url.suffix:/_forms/default.aspx?wa=wsignin1.0}")
	private String landingPageUrlSuffix;
	
	@Value("${sharepoint.online.context.info.url.suffix:/_api/contextinfo}")
	private String contextInfoUrlSuffix;
	

	public SharepointOnlineSession createSession(String securityToken, String siteUrl, String baseFolder) throws TransformerException, URISyntaxException, IOException {
		
		String spEndpointUrl = SharepointUtil.getSPEndpointAddress(siteUrl);
		
		List<String> cookies = getSignInCookies(securityToken, spEndpointUrl);
		String formDigestValue = getFormDigestValue(cookies, spEndpointUrl);
		
		return new SharepointOnlineSession(siteUrl, spEndpointUrl, baseFolder, securityToken, cookies, formDigestValue);
	}
	
	public List<String> getSignInCookies(String securityToken, String spEndpointUrl) throws TransformerException, URISyntaxException {
		
		String landingPageUrl = spEndpointUrl + landingPageUrlSuffix;
		
		RequestEntity<String> requestEntity = new RequestEntity<>(securityToken, HttpMethod.POST, new URI(landingPageUrl));
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		
		HttpHeaders headers = responseEntity.getHeaders();
		List<String> cookies = headers.get("Set-Cookie");
		if (CollectionUtils.isEmpty(cookies)) {
			throw new IllegalStateException("Unable to sign in to sharepoint: no cookies returned in response");
		} 

		return cookies;
	}
	
	public String getFormDigestValue(List<String> cookies, String spEndpointUrl) throws IOException, URISyntaxException, TransformerException {
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Cookie", Joiner.on(';').join(cookies));
		headers.add("Accept", "application/json;odata=verbose");
		headers.add("X-ClientService-ClientTag", "SDK-JAVA");
		
		String contextInfoUrl = spEndpointUrl + contextInfoUrlSuffix;
		RequestEntity<String> requestEntity = new RequestEntity<>(headers, HttpMethod.POST, new URI(contextInfoUrl));
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

		return (String) JsonUtil.getJsonpathValue(responseEntity.getBody(), "d.GetContextWebInformation.FormDigestValue");
	}
	
}
