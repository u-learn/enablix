package com.enablix.app.content.enrich;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;

@Component
public class AuditInfoDateFormatter {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void formatAuditDates(Map fields) {
		formatAuditDates(new MapFieldCollection(fields));
	}
	
	public void formatAuditDates(FieldCollection fieldSet) {
		checkAndFormatDateField(fieldSet, ContentDataConstants.CREATED_AT_KEY);
		checkAndFormatDateField(fieldSet, ContentDataConstants.MODIFIED_AT_KEY);
		checkAndFormatDateField(fieldSet, ContentDataConstants.ARCHIVED_AT_KEY);
	}
	
	private void checkAndFormatDateField(FieldCollection fieldSet, String fieldName) {
		Object obj = fieldSet.get(fieldName);
		if (obj != null && obj instanceof Long) {
			fieldSet.put(fieldName, new Date((Long) obj)); 
		}
	}
	
	public static interface FieldCollection {
		
		public void put(String fieldName, Object value);
		
		public Object get(String fieldName);
	}
	
	public static class MapFieldCollection implements FieldCollection {
		
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
