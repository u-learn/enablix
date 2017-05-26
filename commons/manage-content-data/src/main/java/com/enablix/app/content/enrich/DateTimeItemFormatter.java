package com.enablix.app.content.enrich;

import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class DateTimeItemFormatter implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, TemplateFacade contentTemplate) {
		
		ContainerType containerDef = contentTemplate.getContainerDefinition(updateCtx.contentQId());
		
		for (ContentItemType itemType : containerDef.getContentItem()) {
		
			if (itemType.getType() == ContentItemClassType.DATE_TIME) {
			
				Object object = content.get(itemType.getId());
				
				if (object != null) {
				
					if (object instanceof Number) {
					
						Number numDate = (Number) object;
						content.put(itemType.getId(), new Date(numDate.longValue()));
						
					} else if (object instanceof String) {
						content.put(itemType.getId(), new DateTime((String) object).toDate());
					}
				}
			}
		}
		
	}

}
