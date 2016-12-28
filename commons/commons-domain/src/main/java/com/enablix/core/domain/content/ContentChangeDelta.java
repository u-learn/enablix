package com.enablix.core.domain.content;

import java.util.HashMap;
import java.util.Map;

public class ContentChangeDelta {

	private Map<String, AttributeChange> changedAttributes;

	public ContentChangeDelta() {
		changedAttributes = new HashMap<>();
	}
	
	public Map<String, AttributeChange> getChangedAttributes() {
		return changedAttributes;
	}

	public void addChangedAttribute(AttributeChange attr) {
		changedAttributes.put(attr.getAttributeId(), attr);
	}
	
	public void setChangedAttributes(Map<String, AttributeChange> changedAttributes) {
		this.changedAttributes = changedAttributes;
	}
	
	public boolean hasAttribute(String attributeId) {
		return changedAttributes.containsKey(attributeId);
	}
	
	public static class AttributeChange {
		
		private String attributeId;
		private Object newValue;
		private Object oldValue;
		
		public AttributeChange(String attributeId, Object newValue, Object oldValue) {
			super();
			this.attributeId = attributeId;
			this.newValue = newValue;
			this.oldValue = oldValue;
		}

		public String getAttributeId() {
			return attributeId;
		}
		
		public void setAttributeId(String attributeId) {
			this.attributeId = attributeId;
		}
		
		public Object getNewValue() {
			return newValue;
		}
		
		public void setNewValue(Object newValue) {
			this.newValue = newValue;
		}
		
		public Object getOldValue() {
			return oldValue;
		}

		public void setOldValue(Object oldValue) {
			this.oldValue = oldValue;
		}
		
	}
	
}
