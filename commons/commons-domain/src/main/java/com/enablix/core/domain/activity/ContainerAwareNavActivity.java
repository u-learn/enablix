package com.enablix.core.domain.activity;

public class ContainerAwareNavActivity extends NavigationActivity {

	protected ContainerAwareNavActivity() {
		// for ORM
	}
	
	public ContainerAwareNavActivity(ActivityType activityType, String pageName, String containerQId) {
		super(activityType, pageName);
		setContainerQId(containerQId);
	}

}
