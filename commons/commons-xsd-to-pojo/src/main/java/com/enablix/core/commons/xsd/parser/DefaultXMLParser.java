package com.enablix.core.commons.xsd.parser;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.enablix.commons.constants.AppConstants;

public class DefaultXMLParser<T> implements XMLParser<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultXMLParser.class);

	private Unmarshaller unmarshaller;
	
	private Marshaller marshaller;

	private Class<T> type;
	
	public DefaultXMLParser(String xsdFileLocation, Class<T> type) {

		this.type = type;
		
		try {
			init(xsdFileLocation, type);
		} catch (FactoryConfigurationError | XMLStreamException | SAXException | JAXBException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}
	
	public Marshaller getMarshaller() {
		return marshaller;
	}

	private void init(String xsdFileLocation, Class<?> type) 
			throws FactoryConfigurationError, XMLStreamException, SAXException, JAXBException {
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File(getSchemaURI(xsdFileLocation)));
		final JAXBContext jaxbContext = JAXBContext.newInstance(type);
		
		unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		
		marshaller = jaxbContext.createMarshaller();
		marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	}

	private String getSchemaURI(String xsdFileLocation) {
		String baseDir = System.getProperty(AppConstants.PROPERTY_BASE_DIR, ".");
		LOGGER.debug("Base directory : " + baseDir);
		return baseDir + File.separator + xsdFileLocation;
	}

	@Override
	public Class<T> parseType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T unmarshal(InputStream is) throws JAXBException {
		return (T) getUnmarshaller().unmarshal(is);
	}
	
	@Override
	public void marshal(T input, OutputStream os) throws JAXBException {
		getMarshaller().marshal(input, os);
	}

}
