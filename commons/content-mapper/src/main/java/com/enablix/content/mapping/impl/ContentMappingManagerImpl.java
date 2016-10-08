package com.enablix.content.mapping.impl;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.content.mapper.ContentSource;
import com.enablix.content.mapper.xml.GenericXMLBasedMapper;
import com.enablix.content.mapping.ContentMappingManager;
import com.enablix.content.mapping.repo.ContentMappingRepository;
import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsd.parser.XMLParserRegistry;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.commons.xsdtopojo.ContentMapping;
import com.enablix.core.domain.content.mapping.ContentContainerMappingDocument;

@Component
public class ContentMappingManagerImpl implements ContentMappingManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentMappingManagerImpl.class);
	
	@Autowired
	private ContentMappingCrudService crud;
	
	@Autowired
	private XMLParserRegistry xmlParserRegistry;
	
	@Autowired
	private GenericXMLBasedMapper xmlBasedMapper;
	
	@Autowired
	private ContentMappingRepository repo;

	@Override
	public void save(ContentContainerMappingType mapping, String mapperId, String mapperName) {
		
		ContentContainerMappingDocument mappingDoc = new ContentContainerMappingDocument();
		
		mappingDoc.setIdentity(mapperId + "." + mapping.getQualifiedId());
		mappingDoc.setMapperId(mapperId);
		mappingDoc.setMapperName(mapperName);
		mappingDoc.setContainerMapping(mapping);
		
		crud.saveOrUpdate(mappingDoc);
	}

	@Override
	public void saveXml(InputStream xmlInputStream) throws JAXBException {
		
		XMLParser<ContentMapping> parser = xmlParserRegistry.getXMLParser(ContentMapping.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for ItemCorrelationRules");
			throw new IllegalStateException("No xml parser found for ItemCorrelationRules");
		}
		
		ContentMapping mappings = parser.unmarshal(xmlInputStream);

		for (ContentContainerMappingType mapping : mappings.getContainer()) {
			save(mapping, mappings.getId(), mappings.getName());
		}
		
		xmlBasedMapper.register(mappings);
		
	}

	@Override
	public ContentContainerMappingType getContentMapping(ContentSource source, String contentQId) {
		ContentContainerMappingDocument mappingDoc = repo.findByMapperIdAndContainerMappingQualifiedId(
														source.getSourceId(), contentQId);
		return mappingDoc != null ? mappingDoc.getContainerMapping() : null;
	}

}
