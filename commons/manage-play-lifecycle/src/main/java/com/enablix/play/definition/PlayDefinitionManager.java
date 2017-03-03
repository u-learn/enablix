package com.enablix.play.definition;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import com.enablix.core.commons.xsdtopojo.PlayTemplate;
import com.enablix.core.domain.play.PlayDefinition;

public interface PlayDefinitionManager {

	void savePlayTemplate(PlayTemplate playTemplate);
	
	void savePlayTemplateXml(InputStream xmlInputStream) throws JAXBException;
	
	PlayDefinition getPlayDefinition(String playDefId);
	
	void updateActiveStatus(String playDefId, boolean status);
	
}
