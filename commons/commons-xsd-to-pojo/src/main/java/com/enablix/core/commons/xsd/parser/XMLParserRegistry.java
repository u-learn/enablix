package com.enablix.core.commons.xsd.parser;

public interface XMLParserRegistry {

	XMLParser getXMLParser(Class<?> type);
	
}
