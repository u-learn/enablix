package com.enablix.app.content.kit;

import java.util.List;

import com.enablix.core.domain.content.kit.ContentKit;
import com.enablix.core.domain.content.kit.ContentKitSummary;

public class ContentKitDetail {

	private ContentKit contentKit;
	
	private List<ContentKitSummary> linkedKits;

	public ContentKit getContentKit() {
		return contentKit;
	}

	public void setContentKit(ContentKit contentKit) {
		this.contentKit = contentKit;
	}

	public List<ContentKitSummary> getLinkedKits() {
		return linkedKits;
	}

	public void setLinkedKits(List<ContentKitSummary> linkedKits) {
		this.linkedKits = linkedKits;
	}
	
}
