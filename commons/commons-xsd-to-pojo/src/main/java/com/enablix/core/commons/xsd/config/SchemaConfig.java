package com.enablix.core.commons.xsd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.core.commons.xsd.SchemaConstants;
import com.enablix.core.commons.xsd.parser.ContentTemplateXMLParser;
import com.enablix.core.commons.xsd.parser.DefaultXMLParser;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRules;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRules;

@Configuration
public class SchemaConfig {

	@Bean
	public ContentTemplateXMLParser contentTemplateParser() {
		ContentTemplateXMLParser parser = new ContentTemplateXMLParser(
				SchemaConstants.CONTENT_TEMPLATE_XSD_LOCATION);
		return parser;
	}
	
	@Bean
	public DefaultXMLParser<ItemCorrelationRules> itemItemCorrelationRuleParser() {
		DefaultXMLParser<ItemCorrelationRules> parser = new DefaultXMLParser<ItemCorrelationRules>(
				SchemaConstants.CORR_RULE_TEMPLATE_XSD_LOCATION, ItemCorrelationRules.class);
		return parser;
	}
	
	@Bean
	public DefaultXMLParser<ItemUserCorrelationRules> itemUserCorrelationRuleParser() {
		DefaultXMLParser<ItemUserCorrelationRules> parser = new DefaultXMLParser<ItemUserCorrelationRules>(
				SchemaConstants.CORR_RULE_TEMPLATE_XSD_LOCATION, ItemUserCorrelationRules.class);
		return parser;
	}

}
