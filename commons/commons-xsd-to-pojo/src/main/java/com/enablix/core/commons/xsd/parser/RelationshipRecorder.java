package com.enablix.core.commons.xsd.parser;

import javax.xml.bind.Unmarshaller;

public class RelationshipRecorder extends Unmarshaller.Listener {

	@Override
	public void afterUnmarshal(Object target, Object parent) {
		ParentChildRelation builder = ParentChildRelation.get();
		builder.addRelation(parent, target);
    }
	
}
