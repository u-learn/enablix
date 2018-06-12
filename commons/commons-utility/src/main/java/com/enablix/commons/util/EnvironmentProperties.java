package com.enablix.commons.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="env")
public class EnvironmentProperties {

	private String serverUrl;
	
	private String defaultSubdomain;
	
	private Map<String, String> properties = new HashMap<>();
	
	private ServerUrlBuilder serverUrlBuilder;
	
	public EnvironmentProperties() {
		EnvPropertiesUtil.registerProperties(this);
	}
	
	@PostConstruct
	public void init() {
		this.serverUrlBuilder = new ServerUrlBuilder(getServerUrl(), getDefaultSubdomain());
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getPropValue(String propName) {
		return properties.get(propName);
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public String getSubdomainSpecificServerUrl(String subdomain) {
		return this.serverUrlBuilder.getServerUrl(subdomain);
	}

	public String getDefaultSubdomain() {
		return defaultSubdomain;
	}

	public void setDefaultSubdomain(String defaultSubdomain) {
		this.defaultSubdomain = defaultSubdomain;
	}
	
	private static class ServerUrlBuilder {
		
		private static final String SUBDOMAIN_PLACEHOLDER = ":[subdomain]";
		
		private String serverUrlPropValue;
		private String defaultSubdomain;
		
		private String urlPrefix;
		private boolean variableSubdomain;
		private String baseDomain;
		
		ServerUrlBuilder(String serverUrlPropValue, String defaultSubdomain) {
			this.serverUrlPropValue = serverUrlPropValue;
			this.defaultSubdomain = defaultSubdomain;
			
			int subdomainIndx = serverUrlPropValue.indexOf(SUBDOMAIN_PLACEHOLDER);
			if (subdomainIndx > 0) {
				this.variableSubdomain = true;
				this.urlPrefix = serverUrlPropValue.substring(0, subdomainIndx);
				this.baseDomain = serverUrlPropValue.substring(
						subdomainIndx + SUBDOMAIN_PLACEHOLDER.length(), serverUrlPropValue.length());
			}
		}
		
		String getServerUrl(String subdomain) {
			if (variableSubdomain) {
				String finalSubdomain = StringUtil.hasText(subdomain) ? subdomain : defaultSubdomain;
				return urlPrefix + (StringUtil.hasText(finalSubdomain) ? (finalSubdomain + ".") : "") + baseDomain;
			} 
			return serverUrlPropValue;
		}
		
	}
	
}
