package com.enablix.content.mapping;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import com.enablix.content.mapper.ContentSource;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;

public interface ContentMappingManager {

	void saveXml(InputStream xmlInputStream) throws JAXBException;

	void save(ContentContainerMappingType mapping, String mapperId, String mapperName);
	
	ContentContainerMappingType getContentMapping(ContentSource source, String contentQId);
	
}
