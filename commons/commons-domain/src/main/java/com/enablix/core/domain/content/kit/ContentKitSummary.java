package com.enablix.core.domain.content.kit;

import com.enablix.core.domain.BaseAuditedEntity;

public class ContentKitSummary extends BaseAuditedEntity {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
