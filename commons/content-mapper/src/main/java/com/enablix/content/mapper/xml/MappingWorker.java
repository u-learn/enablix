package com.enablix.content.mapper.xml;

import com.enablix.commons.util.ExecutionOrderAware;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;

public interface MappingWorker extends ExecutionOrderAware {

	void execute(ContentContainerMappingType containerMapping, 
			ExternalContent extContent, EnablixContent ebxContent,
			TemplateFacade template);
	
}
