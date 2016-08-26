package com.enablix.services.util;

import java.util.Calendar;
import java.util.List;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.Activity;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.Actor;
import com.enablix.core.domain.activity.ContentAccessActivity;
import com.enablix.core.domain.activity.ContentActivity;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;
import com.enablix.core.domain.activity.ContentShareActivity;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.domain.activity.DocDownload;
import com.enablix.core.domain.activity.ExternalLinkAccess;
import com.enablix.core.domain.activity.NonRegisteredActor;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.core.ui.DisplayableContent;

public class ActivityLogger {
	
	private ActivityLogger() {
		// object creation not allowed
	}

	public static void auditActivity(ActivityAudit activity) {
		EventUtil.publishEvent(new Event<ActivityAudit>(Events.AUDIT_ACITIVITY, activity));
	}
	
	public static void auditActivity(Activity activity) {
		auditActivity(activity, Channel.WEB);
	}
	
	public static void auditActivity(Activity activity, Channel activityChannel) {
		ProcessContext processContext = ProcessContext.get();
		RegisteredActor actor = new RegisteredActor(processContext.getUserId());
		auditActivity(activity, actor, activityChannel);
	}
	
	public static void auditActivity(Activity activity, Actor actor, Channel activityChannel) {
		
		ActivityAudit activityAudit = new ActivityAudit();
		activityAudit.setActivity(activity);
		
		activityAudit.setActor(actor);
		
		activityAudit.setChannel(new ActivityChannel(activityChannel));
		activityAudit.setActivityTime(Calendar.getInstance().getTime());
		
		auditActivity(activityAudit);
	}
	
	public static void auditContentActivity(ContentActivityType activityType,
			ContentDataRef dataRef, ContainerType containerType) {
		auditContentActivity(activityType, dataRef, containerType, Channel.WEB);
	}
	
	public static void auditContentActivity(ContentActivityType activityType,
			ContentDataRef dataRef, ContainerType containerType, 
			ActivityChannel.Channel activityChannel) {
		
		ContentActivity contentActvy = new ContentActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, activityType);
		
		auditContentActivity(contentActvy, activityChannel);
	}
	
	private static void auditContentActivity(ContentActivity contentActvy, 
			ActivityChannel.Channel activityChannel) {
		
		ProcessContext processContext = ProcessContext.get();
		
		ActivityAudit activity = new ActivityAudit();
		activity.setActivity(contentActvy);
		
		RegisteredActor actor = new RegisteredActor(processContext.getUserId());
		activity.setActor(actor);
		
		activity.setChannel(new ActivityChannel(activityChannel));
		activity.setActivityTime(Calendar.getInstance().getTime());
		
		auditActivity(activity);
	}
	
	public static void auditDocDownload(String contentQId, 
			String contentIdentity, String docIdentity, Channel channel, 
			String contextName, String contextId, String contextTerm) {
		
		ProcessContext processContext = ProcessContext.get();
		auditDocDownload(contentQId, contentIdentity, docIdentity, channel, 
				new RegisteredActor(processContext.getUserId()), contextName, contextId, contextTerm);
	}
	
	public static void auditDocDownload(String contentQId, 
			String contentIdentity, String docIdentity, Channel channel, Actor actor,
			String contextName, String contextId, String contextTerm) {
		
		ActivityAudit activity = new ActivityAudit();
		
		DocDownload docDownloadActvy = new DocDownload(contentIdentity, contentQId, 
				ContainerType.CONTENT, docIdentity, contextName, contextId, contextTerm);
		
		activity.setActivity(docDownloadActvy);
		activity.setActor(actor);
		
		activity.setChannel(new ActivityChannel(channel));
		activity.setActivityTime(Calendar.getInstance().getTime());
		
		auditActivity(activity);
	}

	public static void auditContentShare(String templateId, DisplayableContent content, 
			String sharedWith, ShareMedium sharedFrom, Channel channel, String sharingId) {
		auditContentShareInternal(templateId, content.getRecordIdentity(), 
				content.getContainerQId(), sharedWith, sharedFrom, channel, sharingId);
	}
	
	public static void auditContentShareInternal(String templateId, 
			String contentIdentity, String containerQId, String sharedWith, 
			ShareMedium sharedFrom, Channel channel, String sharingId) {
		
		ContentShareActivity shareActvy = new ContentShareActivity(contentIdentity, 
				containerQId, ContainerType.CONTENT, sharingId, sharedFrom, sharedWith);
		
		auditContentActivity(shareActvy, channel);
	}
	
	public static void auditContentShare(String templateId, List<ContentDataRef> contentList, 
			ShareMedium sharedFrom, Channel channel, String sharingId, String sharedWith) {
		
		for (ContentDataRef content : contentList) {
			auditContentShareInternal(templateId, content.getInstanceIdentity(), 
					content.getContainerQId(), sharedWith, sharedFrom, channel, sharingId);
		}
	}

	public static void auditContentAccess(ContentDataRef dataRef,
			ContainerType containerType, Channel channel) {
		
		ContentActivity contentActvy = new ContentAccessActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType);
		
		auditContentActivity(contentActvy, channel);
	}
	
	public static void auditContentAccess(ContentDataRef dataRef,
			ContainerType containerType, Channel channel, String contextName, String contextId, String contextTerm) {
		
		ContentActivity contentActvy = new ContentAccessActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, contextName, contextId, contextTerm);
		
		auditContentActivity(contentActvy, channel);
	}

	public static void auditExternalLinkAccess(String url, String contentIdentity, 
			String contentItemQId, String aid, Channel channel) {
		
		Activity actvy = new ExternalLinkAccess(url, contentIdentity, contentItemQId);
		Actor actor = null;
		
		ProcessContext processContext = ProcessContext.get();
		if (processContext != null) {
			actor = new RegisteredActor(processContext.getUserId());
		} else if (!StringUtil.isEmpty(aid)) {
			actor = new NonRegisteredActor(aid);
		}
		
		auditActivity(actvy, actor, channel);
		
	}

}
