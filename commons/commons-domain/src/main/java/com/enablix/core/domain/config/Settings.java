package com.enablix.core.domain.config;

import java.util.HashMap;
import java.util.Map;

import com.enablix.core.domain.BaseDocumentEntity;

public abstract class Settings extends BaseDocumentEntity {

	private String key;
	private Map<String, Object> config;

	public Settings() {
		super();
		this.config = new HashMap<>();
	}

	public String getKey() {
		return key;
	}

	public Map<String, Object> getConfig() {
		return config;
	}

	public String getStringValue(String configProp) {
		
		if (config != null) {
		
			Object propValue = config.get(configProp);
			
			if (propValue != null) {
				return String.valueOf(propValue);
			}
		}
		
		return null;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	public void addConfigProperty(String key, Object value) {
		this.config.put(key, value);
	}

}