package com.enablix.analytics.info.detection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.commons.util.collection.CollectionUtil;

public class Assessment {

	private Map<String, TypeOpinion> typeOpinions;
	
	private Map<String, List<LinkOpinion>> linkOpinions;
	
	public Assessment() {
		super();
		this.typeOpinions = new HashMap<>();
		this.linkOpinions = new HashMap<>();
	}

	public Collection<TypeOpinion> getTypeOpinions() {
		return typeOpinions.values();
	}

	public boolean addOpinion(Opinion opinion) {
		
		boolean added = false;
		
		if (opinion instanceof TypeOpinion) {
		
			added = addTypeOpinion((TypeOpinion) opinion);
			
		} else if (opinion instanceof LinkOpinion) {
			
			added = addLinkOpinion((LinkOpinion) opinion);
			
		} else if (opinion instanceof TypeAttrOpinion) {
			
			added = addTypeAttrOpinion((TypeAttrOpinion) opinion);
		}
		
		return added;
	}
	
	protected boolean addTypeOpinion(TypeOpinion opinion) {
		typeOpinions.put(opinion.getContainerQId(), opinion);
		return true;
	}
	
	protected boolean addLinkOpinion(LinkOpinion opinion) {

		return CollectionUtil.addToMappedListValue(
				opinion.getLinkedRecord().getContentQId(), opinion, linkOpinions, 
				() -> new ArrayList<>());
	}
	
	protected boolean addTypeAttrOpinion(TypeAttrOpinion opinion) {
		
		boolean added = false;
		
		TypeOpinion typeOpinion = typeOpinions.get(opinion.getContainerQId());
		
		if (typeOpinion != null) {
			added = typeOpinion.addAttribute(opinion);
		}
		
		return added;
	}

	public void addOpinions(Collection<Opinion> opinions) {
		
		if (opinions != null) {
		
			for (Opinion opinion : opinions) {
				addOpinion(opinion);
			}
		}
	}
	

	public Map<String, List<LinkOpinion>> getLinkOpinions() {
		return linkOpinions;
	}
	
	public List<LinkOpinion> getLinkOpinions(String contentQId) {
		return linkOpinions.get(contentQId);
	}

	@Override
	public String toString() {
		return "Assessment [typeOpinions=" + typeOpinions + ", linkOpinions=" + linkOpinions + "]";
	}

}
