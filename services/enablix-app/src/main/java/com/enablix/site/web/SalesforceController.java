package com.enablix.site.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.security.SecurityUtil;
import com.enablix.core.security.service.EnablixUserService;
import com.google.api.client.util.Value;

import canvas.SignedRequest;

@RestController
public class SalesforceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceController.class);
	
	@Autowired
	private EnablixUserService userService;
	
	@Value("${salesforce.canvas.app.domain:}")
	private String sfCanvasAppDomain;
	
	@Value("${salesforce.canvas.app.secret}")
	private String sfCanvasAppSecret;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value="/sf/canvas/{app}")
	public void salesforceCanvasApp(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String app) throws ServletException, IOException {
		
		// Pull the signed request out of the request body and verify and decode it.
	    Map<String, String[]> parameters = request.getParameterMap();
	    String[] signedRequest = parameters.get("signed_request");
	    LOGGER.debug("Sales force Canvas app secret: ", sfCanvasAppSecret);
	    String signedRequestJson = SignedRequest.verifyAndDecodeAsJson(signedRequest[0], sfCanvasAppSecret);
	    LOGGER.debug("Salesforce signed request: {}", signedRequestJson);
		
		UserDetails currentUser = SecurityUtil.currentUser();
		if (currentUser == null) {
			String userEmail = (String) JsonUtil.getJsonpathValue(signedRequestJson, "context.user.email");
			UserDetails userDetails = userService.loadUserByUsername(userEmail);
			if (userDetails != null) {
				SecurityUtil.loginUser(userDetails);
			}
		}
		
		String appPage = (String) JsonUtil.getJsonpathValue(signedRequestJson, "context.environment.parameters.appPage");
		if (!StringUtil.hasText(appPage)) {
			appPage = "/portal";
		}
		
		StringBuilder htmlPage = new StringBuilder(StringUtil.hasText(sfCanvasAppDomain) ? sfCanvasAppDomain : "");
		htmlPage.append(appPage).append("?ctx_embedded=true");
		
		Object appCtxObj = JsonUtil.getJsonpathValue(signedRequestJson, "context.environment.parameters.appCtx");
	    
		if (appCtxObj instanceof Map) {
	    
			Map<String, Object> appCtx = (Map<String, Object>) appCtxObj;
	    	
	    	for (Map.Entry<String, Object> entry : appCtx.entrySet()) {
	    		
    			htmlPage.append("&")
	    				.append("ctx_").append(entry.getKey())
	    				.append("=")
	    				.append(String.valueOf(entry.getValue()));
	    	}
	    }

	    response.sendRedirect(htmlPage.toString());
	}

}
