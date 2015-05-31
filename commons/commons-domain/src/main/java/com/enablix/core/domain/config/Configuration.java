package com.enablix.core.domain.config;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseEntity;

@Document(collection = "ebx_configuration")
public class Configuration extends BaseEntity {

	private String key;
	
	private Map<String, String> config;

	public Configuration() {
		super();
	}

	public String getKey() {
		return key;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}
	
}
