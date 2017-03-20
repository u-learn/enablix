package com.enablix.analytics.search.es;

public class ESPropertiesUtil {

	private static ElasticsearchProperties properties;
	
	public static void registerProperties(ElasticsearchProperties elasticsearchProperties) {
		ESPropertiesUtil.properties = elasticsearchProperties;
	}
	
	public static ElasticsearchProperties getProperties() {
		return properties;
	}
	
}
