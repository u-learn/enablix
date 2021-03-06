package com.enablix.core.commons.xsd.parser;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@SuppressWarnings("rawtypes")
@Component
public class DefaultXMLParserFactory extends SpringBackedAbstractFactory<XMLParser> implements XMLParserRegistry {

	@Override
	protected Class<XMLParser> lookupForType() {
		return XMLParser.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> XMLParser<T> getXMLParser(Class<T> type) {
		for (XMLParser parser : registeredInstances()) {
			if (parser.parseType() == type) {
				return parser;
			}
		}

		throw new IllegalStateException("No parser found for type [" + type.getName() + "]");
	}

}
