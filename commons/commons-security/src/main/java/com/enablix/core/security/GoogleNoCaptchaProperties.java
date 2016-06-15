package com.enablix.core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleNoCaptchaProperties {

	@Value("${google.nocaptcha.validation.url}")
	private String captchaValidationUrl;
	
	@Value("${google.nocaptcha.server.secret.key}")
	private String serverSecretKey;
	
	@Value("${google.nocaptcha.client.site.key}")
	private String clientSiteKey;

	public String getCaptchaValidationUrl() {
		return captchaValidationUrl;
	}

	public String getServerSecretKey() {
		return serverSecretKey;
	}

	public String getClientSiteKey() {
		return clientSiteKey;
	}
	
}
