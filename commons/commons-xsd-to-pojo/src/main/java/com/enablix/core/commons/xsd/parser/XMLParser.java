package com.enablix.core.commons.xsd.parser;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

public interface XMLParser<T> {

	Class<T> parseType();
	
	T unmarshal(InputStream is) throws JAXBException;

	void marshal(T input, OutputStream os) throws JAXBException;
	
}
