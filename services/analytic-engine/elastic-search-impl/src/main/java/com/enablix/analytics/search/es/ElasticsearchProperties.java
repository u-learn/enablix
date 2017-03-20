package com.enablix.analytics.search.es;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;

@Component
@ConfigurationProperties(prefix="elasticsearch")
public class ElasticsearchProperties {

	private static final String FUZZY_MATCH_ON_KEY = "fuzzy.match.on";
	private static final String FUZZY_MATCH_MIN_CHARS_KEY = "fuzzy.match.min.chars";
	
	private Map<String, String> properties = new HashMap<>();
	
	private boolean fuzzyMatchOn = true;
	
	private int fuzzyMatchMinChars = 5;
	
	public ElasticsearchProperties() {
		ESPropertiesUtil.registerProperties(this);
	}
	
	@PostConstruct
	private void init() {
		
		String fuzzyMatchOnStr = properties.get(FUZZY_MATCH_ON_KEY);
		if (StringUtil.hasText(fuzzyMatchOnStr)) {
			fuzzyMatchOn = Boolean.parseBoolean(fuzzyMatchOnStr);
		}
		
		String fuzzyMatchMinCharsStr = properties.get(FUZZY_MATCH_MIN_CHARS_KEY);
		if (StringUtil.hasText(fuzzyMatchMinCharsStr)) {
			fuzzyMatchMinChars = Integer.parseInt(fuzzyMatchMinCharsStr);
		}
		
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

	public boolean isFuzzyMatchOn() {
		return fuzzyMatchOn;
	}

	public int getFuzzyMatchMinChars() {
		return fuzzyMatchMinChars;
	}

	
}
