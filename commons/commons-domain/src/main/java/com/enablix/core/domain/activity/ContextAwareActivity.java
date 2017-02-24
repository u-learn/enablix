package com.enablix.core.domain.activity;

import com.enablix.core.activity.audit.ActivityContextAware;

public abstract class ContextAwareActivity extends Activity implements ActivityContextAware {

	private String activityOrigin;
	
	private String contextId;
	
	private String contextName;
	
	private String contextTerm;
	
	protected ContextAwareActivity(Category category) {
		super(category);
	}

	public String getActivityOrigin() {
		return activityOrigin;
	}

	@Override
	public void setActivityOrigin(String activityOrigin) {
		this.activityOrigin = activityOrigin;
	}

	public String getContextId() {
		return contextId;
	}

	@Override
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getContextName() {
		return contextName;
	}

	@Override
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getContextTerm() {
		return contextTerm;
	}

	@Override
	public void setContextTerm(String contextTerm) {
		this.contextTerm = contextTerm;
	}

}
