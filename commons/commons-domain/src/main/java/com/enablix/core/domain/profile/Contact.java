package com.enablix.core.domain.profile;

import com.enablix.core.domain.BaseWrappedEntity;
import com.enablix.core.domain.enumtype.ContactCategory;

public abstract class Contact extends BaseWrappedEntity {

	private ContactCategory category;
	
	private boolean primary;

	public ContactCategory getCategory() {
		return category;
	}

	public void setCategory(ContactCategory category) {
		this.category = category;
	}

	public boolean isPrimary() {
		return primary;
	}

	protected void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
}
