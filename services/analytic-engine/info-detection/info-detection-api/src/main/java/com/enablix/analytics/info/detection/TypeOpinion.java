package com.enablix.analytics.info.detection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.commons.util.collection.CollectionUtil;

public class TypeOpinion extends Opinion {

	private String containerQId; // opinion type
	
	private Map<String, List<TypeAttrOpinion>> attributes;
	
	protected TypeOpinion() {
		// for ORM
	}
	
	public TypeOpinion(String containerQId, String opinionBy, float confidence) {
		super(opinionBy, confidence);
		this.containerQId = containerQId;
		this.attributes = new HashMap<>();
	}

	public String getContainerQId() {
		return containerQId;
	}

	public Collection<TypeAttrOpinion> getAttributeOpinions(String attrId) {
		return attributes.get(attrId);
	}
	
	public Map<String, List<TypeAttrOpinion>> getAttributes() {
		return attributes;
	}

	boolean addAttribute(TypeAttrOpinion opinion) {
		
		return CollectionUtil.addToMappedListValue(
				opinion.getAttributeId(), opinion, attributes, () -> new ArrayList<>());
	}

	@Override
	public String toString() {
		return "TypeOpinion [containerQId=" + containerQId + ", attributes=" + attributes + "]";
	}
	
}
