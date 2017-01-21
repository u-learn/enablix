package com.enablix.play.definition.impl;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsd.parser.XMLParserRegistry;
import com.enablix.core.commons.xsdtopojo.PlayTemplate;
import com.enablix.core.domain.play.PlayDefinition;
import com.enablix.play.definition.PlayDefinitionManager;

@Component
public class PlayDefinitionManagerImpl implements PlayDefinitionManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlayDefinitionManagerImpl.class);
	
	@Autowired
	private PlayDefinitionCrudService crudService;
	
	@Autowired
	private XMLParserRegistry xmlParserRegistry;
	
	@Override
	public void savePlayTemplate(PlayTemplate playTemplate) {
		
		PlayDefinition playDef = new PlayDefinition();
		
		if (StringUtil.isEmpty(playTemplate.getId())) {
			playTemplate.setId(IdentityUtil.generateIdentity(playTemplate));
		}
		
		playDef.setId(playTemplate.getId());
		playDef.setPlayTemplate(playTemplate);
		crudService.saveOrUpdate(playDef);
	}

	@Override
	public void savePlayTemplateXml(InputStream xmlInputStream) throws JAXBException {
		
		XMLParser<PlayTemplate> parser = xmlParserRegistry.getXMLParser(PlayTemplate.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for Triggers");
			throw new IllegalStateException("No xml parser found for Triggers");
		}
		
		PlayTemplate playTemplate = parser.unmarshal(xmlInputStream);
		savePlayTemplate(playTemplate);
		
	}

	@Override
	public PlayDefinition getPlayDefinition(String playDefId) {
		return crudService.getRepository().findOne(playDefId);
	}

}
