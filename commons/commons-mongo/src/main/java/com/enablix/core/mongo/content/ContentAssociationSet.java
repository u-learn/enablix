package com.enablix.core.mongo.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContentAssociationSet {

	private Map<String, Collection<ContentAssociation>> associations;

	public ContentAssociationSet() {
		super();
		this.associations = new HashMap<>();
	}
	
	public void addAssociation(ContentAssociation association) {
		Collection<ContentAssociation> assocColl = associations.get(association.getAssociationName());
		if (assocColl == null) {
			assocColl = new ArrayList<ContentAssociation>();
			associations.put(association.getAssociationName(), assocColl);
		}
		assocColl.add(association);
	}
	
}
