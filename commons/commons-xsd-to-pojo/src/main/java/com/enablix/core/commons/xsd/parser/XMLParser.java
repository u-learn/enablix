package com.enablix.core.commons.xsd.parser;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

public interface XMLParser<T> {

	Class<T> parseType();
	
	T unmarshal(InputStream is) throws JAXBException;
	
}
