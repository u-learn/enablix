package com.enablix.core.commons.xsd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.core.commons.xsd.SchemaConstants;
import com.enablix.core.commons.xsd.parser.ContentTemplateXMLParser;

@Configuration
public class SchemaConfig {

	@Bean
	public ContentTemplateXMLParser contentTemplateParser() {
		ContentTemplateXMLParser parser = new ContentTemplateXMLParser(
				SchemaConstants.CONTENT_TEMPLATE_XSD_LOCATION);
		return parser;
	}

}
