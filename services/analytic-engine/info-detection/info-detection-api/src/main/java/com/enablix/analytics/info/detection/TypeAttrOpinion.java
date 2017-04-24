package com.enablix.analytics.info.detection;

public class TypeAttrOpinion extends Opinion {

	private String containerQId;
	
	private String attributeId;
	
	private Object value;

	public TypeAttrOpinion(String containerQId, String attributeId, 
			Object value, String opinionBy, float confidence) {
		
		super(opinionBy, confidence);
		
		this.containerQId = containerQId;
		this.attributeId = attributeId;
		this.value = value;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public String getAttributeId() {
		return attributeId;
	}

	public Object getValue() {
		return value;
	}
	
}
