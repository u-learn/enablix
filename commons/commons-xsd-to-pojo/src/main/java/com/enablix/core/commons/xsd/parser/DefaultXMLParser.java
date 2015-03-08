package com.enablix.core.commons.xsd.parser;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.enablix.core.commons.xsd.SchemaConstants;

public class DefaultXMLParser implements XMLParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultXMLParser.class);

	private Unmarshaller unmarshaller;

	private Class<?> type;
	
	public DefaultXMLParser(String xsdFileLocation, Class<?> type) {

		this.type = type;
		
		try {
			init(xsdFileLocation, type);
		} catch (FactoryConfigurationError | XMLStreamException | SAXException | JAXBException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}

	private void init(String xsdFileLocation, Class<?> type) 
			throws FactoryConfigurationError, XMLStreamException, SAXException, JAXBException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File(getSchemaURI(xsdFileLocation)));
		final JAXBContext jaxbContext = JAXBContext.newInstance(type);
		unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
	}

	private String getSchemaURI(String xsdFileLocation) {
		String baseDir = System.getProperty(SchemaConstants.PROPERTY_BASE_DIR, ".");
		LOGGER.debug("Base directory : " + baseDir);
		return baseDir + File.separator + xsdFileLocation;
	}

	@Override
	public Class<?> parseType() {
		return type;
	}

}
