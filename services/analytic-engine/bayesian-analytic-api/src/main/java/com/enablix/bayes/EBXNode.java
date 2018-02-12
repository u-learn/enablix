package com.enablix.bayes;

import java.util.Collection;

import norsys.netica.Node;

public interface EBXNode {
	
	Node getNode();

	String getTitle();

	String getName();

	void linkTo(EBXNode node);

	void linkFrom(EBXNode node);

	void setCPTable(float[] props);
	
	Collection<String> getStates();
	
	float getBelief(String state);
}
