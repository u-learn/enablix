package com.enablix.app.content.enrich;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.BaseEntity;
import com.enablix.services.util.TemplateUtil;
import com.mongodb.DBObject;

@Component
public class AuditInfoEnricher extends AbstractMongoEventListener<BaseEntity> implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, 
			Map<String, Object> content, ContentTemplate contentTemplate) {
		
		ContainerType containerDef = TemplateUtil.findContainer(
				contentTemplate.getDataDefinition(), updateCtx.contentQId());
		
		setAuditInfoInHierarchy(content, containerDef);
	}
	
	private void setAuditInfoInHierarchy(
			final Map<String, Object> containerData, ContainerType containerDef) {
		
		setAuditInfo(new FieldCollection() {

			@Override
			public void put(String fieldName, Object value) {
				containerData.put(fieldName, value);
			}

			@Override
			public Object get(String fieldName) {
				return containerData.get(fieldName);
			}
			
		});
		
		// populate identity in hierarchy
		for (ContainerType childContainer : containerDef.getContainer()) {
			Object childContainerData = containerData.get(childContainer.getId());
			if (childContainerData != null) {
				setAuditInfoIfContainer(childContainerData, childContainer);
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void setAuditInfoIfContainer(Object obj, ContainerType containerDef) {
		
		if (obj instanceof Map<?,?>) {
			Map<String, Object> mapObj = (Map<String, Object>) obj; 
			setAuditInfoInHierarchy(mapObj, containerDef);
			
		} else if (obj instanceof Collection<?>) {
			for (Object collElement : (Collection<?>) obj) {
				setAuditInfoIfContainer(collElement, containerDef);
			}
		}
		
	}
	
	private void setAuditInfo(FieldCollection fieldSet) {
		
		Date currDate = Calendar.getInstance().getTime();
		String userId = ProcessContext.get().getUserId();
		String username = ProcessContext.get().getUserDisplayName();
		
		Object obj = fieldSet.get(ContentDataConstants.CREATED_AT_KEY);
		if (obj == null) {
			fieldSet.put(ContentDataConstants.CREATED_AT_KEY, currDate); 
			
		} else {
			if (obj instanceof Long) {
				Date createdAt = new Date((Long) obj);
				fieldSet.put(ContentDataConstants.CREATED_AT_KEY, createdAt);
			}
		}

		if (fieldSet.get(ContentDataConstants.CREATED_BY_KEY) == null) {
			fieldSet.put(ContentDataConstants.CREATED_BY_KEY, userId);
			fieldSet.put(ContentDataConstants.CREATED_BY_NAME_KEY, username);
		}

		fieldSet.put(ContentDataConstants.MODIFIED_BY_KEY, userId);
		fieldSet.put(ContentDataConstants.MODIFIED_BY_NAME_KEY, username);
		
		fieldSet.put(ContentDataConstants.MODIFIED_AT_KEY, currDate);
		
	}

	public void onBeforeSave(final BaseEntity source, final DBObject dbo) {
		setAuditInfo(new FieldCollection() {

			@Override
			public void put(String fieldName, Object value) {
				dbo.put(fieldName, value);
			}

			@Override
			public Object get(String fieldName) {
				return dbo.get(fieldName);
			}
			
		});
	}
	
	private static interface FieldCollection {
		
		public void put(String fieldName, Object value);
		
		public Object get(String fieldName);
	}
	
}
