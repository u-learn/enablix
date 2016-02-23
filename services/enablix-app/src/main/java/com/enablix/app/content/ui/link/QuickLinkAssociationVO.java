package com.enablix.app.content.ui.link;

import com.enablix.core.domain.links.QuickLinkCategory;

public class QuickLinkAssociationVO {

	private QuickLinkCategory category;
	
	private boolean associated;
	
	private String quickLinkIdentity;
	
	public QuickLinkAssociationVO() {
		
	}

	public QuickLinkAssociationVO(QuickLinkCategory category, boolean associated, 
			String quickLinkIdentity) {
		super();
		this.category = category;
		this.associated = associated;
		this.quickLinkIdentity = quickLinkIdentity;
	}

	public QuickLinkCategory getCategory() {
		return category;
	}

	public void setCategory(QuickLinkCategory category) {
		this.category = category;
	}

	public boolean isAssociated() {
		return associated;
	}

	public void setAssociated(boolean associated) {
		this.associated = associated;
	}

	public String getQuickLinkIdentity() {
		return quickLinkIdentity;
	}

	public void setQuickLinkIdentity(String quickLinkIdentity) {
		this.quickLinkIdentity = quickLinkIdentity;
	}
	
}
