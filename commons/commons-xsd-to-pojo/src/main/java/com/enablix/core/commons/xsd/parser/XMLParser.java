package com.enablix.core.commons.xsd.parser;

import javax.xml.bind.Unmarshaller;

public interface XMLParser {

	Unmarshaller getUnmarshaller();
	
	Class<?> parseType();
	
}
