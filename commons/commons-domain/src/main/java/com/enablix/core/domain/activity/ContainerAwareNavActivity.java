package com.enablix.core.domain.activity;

public class ContainerAwareNavActivity extends NavigationActivity {

	private String containerQId;
	
	protected ContainerAwareNavActivity() {
		// for ORM
	}
	
	public ContainerAwareNavActivity(ActivityType activityType, String pageName, String containerQId) {
		super(activityType, pageName);
		this.containerQId = containerQId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

}
