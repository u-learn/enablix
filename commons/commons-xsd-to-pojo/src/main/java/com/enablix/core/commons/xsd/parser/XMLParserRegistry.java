package com.enablix.core.commons.xsd.parser;

public interface XMLParserRegistry {

	<T> XMLParser<T> getXMLParser(Class<T> type);
	
}
