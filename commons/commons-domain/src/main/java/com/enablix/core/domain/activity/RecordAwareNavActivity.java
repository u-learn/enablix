package com.enablix.core.domain.activity;

public class RecordAwareNavActivity extends ContainerAwareNavActivity {

	private String itemIdentity;
	
	private String itemTitle;
	
	@SuppressWarnings("unused")
	private RecordAwareNavActivity() {
		// for ORM
	}
	
	public RecordAwareNavActivity(String pageName, String containerQId, String itemIdentity, String itemTitle) {
		super(pageName, containerQId);
		this.itemIdentity = itemIdentity;
		this.itemTitle = itemTitle;
	}

	public String getItemIdentity() {
		return itemIdentity;
	}

	public void setItemIdentity(String itemIdentity) {
		this.itemIdentity = itemIdentity;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

}
