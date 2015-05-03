package com.enablix.app.content.enrich;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

@Component
public class AuditInfoEnricher implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, 
			Map<String, Object> content, ContentTemplate contentTemplate) {
		setAuditInfoInHierarchy(content);
	}
	
	private void setAuditInfoInHierarchy(Map<String, Object> containerData) {
		
		setAuditInfo(containerData);
		
		// populate identity in hierarchy
		for (Map.Entry<String, Object> entry : containerData.entrySet()) {
			setAuditInfoIfContainer(entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setAuditInfoIfContainer(Object obj) {
		
		if (obj instanceof Map<?,?>) {
			Map<String, Object> mapObj = (Map<String, Object>) obj; 
			setAuditInfoInHierarchy(mapObj);
			
		} else if (obj instanceof Collection<?>) {
			for (Object collElement : (Collection<?>) obj) {
				setAuditInfoIfContainer(collElement);
			}
		}
		
	}
	
	private void setAuditInfo(Map<String, Object> containerData) {
		
		Date currDate = Calendar.getInstance().getTime();
		
		Object obj = containerData.get(ContentDataConstants.CREATED_AT_KEY);
		if (obj == null) {
			containerData.put(ContentDataConstants.CREATED_AT_KEY, currDate); 
		}
		
		containerData.put(ContentDataConstants.MODIFIED_AT_KEY, currDate); 
	}

}
