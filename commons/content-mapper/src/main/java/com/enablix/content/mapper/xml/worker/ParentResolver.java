package com.enablix.content.mapper.xml.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.content.mapper.EnablixContent;
import com.enablix.content.mapper.ExternalContent;
import com.enablix.content.mapper.xml.MappingWorker;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentContainerMappingType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.ParentContainerMappingType;

@Component
public class ParentResolver implements MappingWorker {

	@Autowired
	private ContainerMappingHandler containerMappingHandler;
	
	@Override
	public float executionOrder() {
		return MappingWorkerExecOrder.PARENT_RESOLVER;
	}

	@Override
	public void execute(ContentContainerMappingType containerMapping, ExternalContent extContent,
			EnablixContent ebxContent, ContentTemplate template) {
		
		ParentContainerMappingType parent = containerMapping.getParent();
		
		if (parent != null) {
		
			ContentDataRecord containerRecord = containerMappingHandler.getContainerRecord(
					containerMapping, parent.getContainerMapping(), extContent, template);
			
			if (containerRecord != null) {
				String parentIdentity = (String) containerRecord.getRecord().get(ContentDataConstants.IDENTITY_KEY);
				ebxContent.setParentIdentity(parentIdentity);
			}
			
		}
		
	}

}
