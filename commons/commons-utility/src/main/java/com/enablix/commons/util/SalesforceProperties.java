package com.enablix.commons.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SalesforceProperties {

	@Value("${salesforce.canvas.app.domain:}")
	private String canvasAppDomain;
	
	@Value("${salesforce.canvas.app.secret}")
	private String canvasAppSecret;

	public String getCanvasAppDomain() {
		return canvasAppDomain;
	}

	public String getCanvasAppSecret() {
		return canvasAppSecret;
	}
	
}
