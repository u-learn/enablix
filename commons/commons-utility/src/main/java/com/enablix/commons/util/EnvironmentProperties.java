package com.enablix.commons.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="env")
public class EnvironmentProperties {

	private static final String SERVER_URL = "server.url";
	
	private Map<String, String> properties = new HashMap<>();
	
	public EnvironmentProperties() {
		EnvPropertiesUtil.registerProperties(this);
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
		return getPropValue(SERVER_URL);
	}
	
}
