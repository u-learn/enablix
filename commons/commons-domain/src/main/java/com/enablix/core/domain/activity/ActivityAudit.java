package com.enablix.core.domain.activity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ebx_activity_audit")
public class ActivityAudit {

	private Activity activity;
	
	private ActivityChannel<?> channel;
	
	private Date activityTime;
	
	private Actor actor;

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ActivityChannel<?> getChannel() {
		return channel;
	}

	public void setChannel(ActivityChannel<?> channel) {
		this.channel = channel;
	}

	public Date getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
}
