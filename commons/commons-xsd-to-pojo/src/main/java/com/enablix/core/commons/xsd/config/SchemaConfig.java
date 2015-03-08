package com.enablix.core.commons.xsd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.core.commons.xsd.SchemaConstants;
import com.enablix.core.commons.xsd.parser.DefaultXMLParser;
import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

@Configuration
public class SchemaConfig {

	@Bean
	public XMLParser contentTemplateParser() {
		XMLParser parser = new DefaultXMLParser(SchemaConstants.CONTENT_TEMPLATE_XSD_LOCATION, ContentTemplate.class);
		return parser;
	}

}
