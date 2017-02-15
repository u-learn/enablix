package com.enablix.app.content.enrich;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.BaseDocumentEntity;
import com.enablix.services.util.template.TemplateWrapper;
import com.mongodb.DBObject;

@Component
public class AuditInfoEnricher extends AbstractMongoEventListener<BaseDocumentEntity> implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, 
			Map<String, Object> content, TemplateWrapper contentTemplate) {
		
		ContainerType containerDef = contentTemplate.getContainerDefinition(updateCtx.contentQId());
		
		setAuditInfoInHierarchy(content, containerDef);
		
		// format audit dates if exists in any child structure
		formatAuditDatesInHierarchy(content);
	}
	
	private void setAuditInfoInHierarchy(
			final Map<String, Object> containerData, ContainerType containerDef) {
		
		setAuditInfo(new MapFieldCollection(containerData));
		
		// populate audit info in hierarchy
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void formatAuditDatesInHierarchy(final Map<String, Object> dataMap) {
		
		formatAuditDates(new MapFieldCollection(dataMap));
		
		for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
			
			Object value = entry.getValue();
			if (value instanceof Map) {
				
				formatAuditDatesInHierarchy((Map) value);
				
			} else if (value instanceof List) {
				
				for (Object listEntry : (List) value) {
					if (value instanceof Map) {
						formatAuditDatesInHierarchy((Map) listEntry);
					}
				}
			}
		}
	}
	
	private void formatAuditDates(FieldCollection fieldSet) {
		checkAndFormatDateField(fieldSet, ContentDataConstants.CREATED_AT_KEY);
		checkAndFormatDateField(fieldSet, ContentDataConstants.MODIFIED_AT_KEY);
	}
	
	private void checkAndFormatDateField(FieldCollection fieldSet, String fieldName) {
		Object obj = fieldSet.get(fieldName);
		if (obj != null && obj instanceof Long) {
			fieldSet.put(fieldName, new Date((Long) obj)); 
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

	public void onBeforeSave(final BaseDocumentEntity source, final DBObject dbo) {
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
		
		source.setCreatedBy((String) dbo.get(ContentDataConstants.CREATED_BY_KEY));
		source.setCreatedByName((String) dbo.get(ContentDataConstants.CREATED_BY_NAME_KEY));
		source.setCreatedAt((Date) dbo.get(ContentDataConstants.CREATED_AT_KEY));
		
		source.setModifiedBy((String) dbo.get(ContentDataConstants.MODIFIED_BY_KEY));
		source.setModifiedByName((String) dbo.get(ContentDataConstants.MODIFIED_BY_NAME_KEY));
		source.setModifiedAt((Date) dbo.get(ContentDataConstants.MODIFIED_AT_KEY));
		
	}
	
	private static interface FieldCollection {
		
		public void put(String fieldName, Object value);
		
		public Object get(String fieldName);
	}
	
	private static class MapFieldCollection implements FieldCollection {
		
		private Map<String, Object> fields;
		
		MapFieldCollection(Map<String, Object> fields) {
			this.fields = fields;
		}
		
		@Override
		public void put(String fieldName, Object value) {
			fields.put(fieldName, value);
		}

		@Override
		public Object get(String fieldName) {
			return fields.get(fieldName);
		}
		
	}
	
}
