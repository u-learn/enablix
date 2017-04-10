package com.enablix.data.segment.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataSegmentDefinitionType;
import com.enablix.core.domain.segment.DataSegmentSpec;
import com.enablix.core.mq.EventSubscription;
import com.enablix.data.segment.repo.DataSegmentSpecRepository;
import com.enablix.data.segment.view.DataSegmentConnector;

@Component
public class DataSegmentUpdateSpecHandler {

	@Autowired
	private DataSegmentSpecRepository dsSpecRepo;
	
	@Autowired
	private DataSegmentConnectorRegistry connRegistry;
	
	@EventSubscription(eventName = Events.CONTENT_TEMPLATE_UPDATED)
	public void updateDataSegmentAwareEntities(ContentTemplate template) {
		
		DataSegmentDefinitionType dsDefinition = template.getDataSegmentDefinition();
		
		if (dsDefinition != null) {
			
			DataSegmentSpec existSpec = dsSpecRepo.findByTemplateId(template.getId());
			
			if (existSpec == null || !matches(dsDefinition, existSpec.getDefinition())) {
				executeConnectors(template, dsDefinition);
			}
			
			if (existSpec == null) {
				existSpec = new DataSegmentSpec();
				existSpec.setTemplateId(template.getId());
			}
			
			existSpec.setDefinition(dsDefinition);
			dsSpecRepo.save(existSpec);
			
		}
		
	}

	private boolean matches(DataSegmentDefinitionType dsDefinition, DataSegmentDefinitionType existDef) {

		if (dsDefinition == null && existDef == null) {
			return true;
		} else if (dsDefinition == null && existDef != null) {
			return false;
		} else if (dsDefinition != null && existDef == null) {
			return false;
		} else {

			if (dsDefinition.getSegmentAttr().size() != existDef.getSegmentAttr().size()) {
				return false;
			} else {
				
				for (ContentItemType attr : dsDefinition.getSegmentAttr()) {
					
					boolean attrMatchFound = false;
					
					for (ContentItemType existAttr : existDef.getSegmentAttr()) {
						
						if (attr.getId().equals(existAttr.getId())) {
							attrMatchFound = true;
							break;
						}
					}
					
					if (!attrMatchFound) {
						return false;
					}
				}
			}
		}
		
		return true;
	}

	private void executeConnectors(ContentTemplate template, DataSegmentDefinitionType dataSegmentDef) {
		for (DataSegmentConnector<?> conn : connRegistry.getAllConnectors()) {
			conn.updateAllRecords(template, dataSegmentDef);
		}
		
	}
	
}
