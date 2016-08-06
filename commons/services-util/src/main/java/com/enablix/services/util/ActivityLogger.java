package com.enablix.services.util;

import java.util.Calendar;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.Actor;
import com.enablix.core.domain.activity.ContentActivity;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;
import com.enablix.core.domain.activity.DocDownload;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;

public class ActivityLogger {

	public static void auditActivity(ActivityAudit activity) {
		EventUtil.publishEvent(new Event<ActivityAudit>(Events.AUDIT_ACITIVITY, activity));
	}
	
	public static void auditContentActivity(ContentActivityType activityType,
			ContentDataRef dataRef, ContainerType containerType) {
		auditContentActivity(activityType, dataRef, containerType, Channel.WEB);
	}
	
	public static void auditContentActivity(ContentActivityType activityType,
			ContentDataRef dataRef, ContainerType containerType, ActivityChannel.Channel activityChannel) {
		
		ProcessContext processContext = ProcessContext.get();
		
		ActivityAudit activity = new ActivityAudit();
		
		ContentActivity contentActvy = new ContentActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, activityType);
		
		activity.setActivity(contentActvy);
		
		RegisteredActor actor = new RegisteredActor(processContext.getUserId());
		activity.setActor(actor);
		
		activity.setChannel(new ActivityChannel(activityChannel));
		activity.setActivityTime(Calendar.getInstance().getTime());
		
		auditActivity(activity);
	}
	
	public static void auditDocDownload(String contentQId, 
			String contentIdentity, String docIdentity, Channel channel) {
		
		ProcessContext processContext = ProcessContext.get();
		auditDocDownload(contentQId, contentIdentity, docIdentity, 
				channel, new RegisteredActor(processContext.getUserId()));
	}
	
	public static void auditDocDownload(String contentQId, 
			String contentIdentity, String docIdentity, Channel channel, Actor actor) {
		
		ActivityAudit activity = new ActivityAudit();
		
		DocDownload docDownloadActvy = new DocDownload(
				contentIdentity, contentQId, ContainerType.CONTENT, docIdentity);
		
		activity.setActivity(docDownloadActvy);
		activity.setActor(actor);
		
		activity.setChannel(new ActivityChannel(channel));
		activity.setActivityTime(Calendar.getInstance().getTime());
		
		auditActivity(activity);
	}

}
