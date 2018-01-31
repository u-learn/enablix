package com.enablix.services.util;

import java.util.Calendar;
import java.util.List;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.activity.audit.ActivityContextAware;
import com.enablix.core.activity.audit.ActivityTrackingContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.Activity;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.Actor;
import com.enablix.core.domain.activity.ContentAccessActivity;
import com.enablix.core.domain.activity.ContentActivity;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentConnActivity;
import com.enablix.core.domain.activity.ContentDownldURLCopy;
import com.enablix.core.domain.activity.ContentExtLinkURLCopy;
import com.enablix.core.domain.activity.ContentPortalURLCopy;
import com.enablix.core.domain.activity.ContentShareActivity;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.core.domain.activity.DocumentActivity;
import com.enablix.core.domain.activity.ExternalLinkAccess;
import com.enablix.core.domain.activity.NonRegisteredActor;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.core.ui.DisplayableContent;

public class ActivityLogger {
	
	private ActivityLogger() {
		// object creation not allowed
	}

	public static void auditActivity(ActivityAudit activity) {
		
		Activity activityObj = activity.getActivity();
		if (activityObj instanceof ActivityContextAware) {
			ActivityTrackingContext.get().setActivityContext((ActivityContextAware) activityObj);
		}
		
		EventUtil.publishEvent(new Event<ActivityAudit>(Events.AUDIT_ACITIVITY, activity));
	}
	
	public static void auditActivity(Activity activity) {
		Channel channel = ActivityTrackingContext.get().getActivityChannel(Channel.WEB);
		auditActivity(activity, channel);
	}
	
	public static void auditActivity(Activity activity, Channel activityChannel) {
		ProcessContext pc = ProcessContext.get();
		RegisteredActor actor = new RegisteredActor(pc.getUserId(), pc.getUserDisplayName());
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
	
	public static void auditContentActivity(ActivityType activityType,
			ContentDataRef dataRef, ContainerType containerType, Actor actor) {
		Channel channel = ActivityTrackingContext.get().getActivityChannel(Channel.WEB);
		auditContentActivity(activityType, dataRef, containerType, channel, actor);
	}
	
	public static void auditContentActivity(ActivityType activityType,
			ContentDataRef dataRef, ContainerType containerType, 
			ActivityChannel.Channel activityChannel, Actor actor) {
		
		ContentActivity contentActvy = new ContentActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, activityType, dataRef.getTitle());
		
		ActivityTrackingContext atCtx = ActivityTrackingContext.get();
		
		contentActvy.setActivityOrigin(atCtx.getActivityOrigin());
		contentActvy.setContextId(atCtx.getActivityContextId());
		contentActvy.setContextName(atCtx.getActivityContextName());
		contentActvy.setContextTerm(atCtx.getActivityContextTerm());
		
		auditContentActivity(contentActvy, activityChannel, actor);
	}
	
	public static void auditContentPortalURLCopied(ContentDataRef dataRef,
			ContainerType containerType, Channel channel) {
		
		ContentActivity contentActvy = new ContentPortalURLCopy(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, dataRef.getTitle());
		
		auditContentActivity(contentActvy, channel);
	}
	
	public static void auditContentDownldURLCopied(ContentDataRef dataRef,
			ContainerType containerType, Channel channel) {
		
		ContentActivity contentActvy = new ContentDownldURLCopy(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, dataRef.getTitle());
		
		auditContentActivity(contentActvy, channel);
	}
	
	public static void auditContentActivity(ContentActivity contentActvy, 
			ActivityChannel.Channel activityChannel) {
		auditContentActivity(contentActvy, activityChannel, null);
	}
	
	public static void auditContentActivity(ContentActivity contentActvy, 
			ActivityChannel.Channel activityChannel, Actor actor) {
		
		ProcessContext pc = ProcessContext.get();
		
		ActivityAudit activity = new ActivityAudit();
		activity.setActivity(contentActvy);
		
		if (actor == null) {
			actor = new RegisteredActor(pc.getUserId(), pc.getUserDisplayName());
		}
		
		activity.setActor(actor);
		
		activity.setChannel(new ActivityChannel(activityChannel));
		activity.setActivityTime(Calendar.getInstance().getTime());
		
		auditActivity(activity);
	}
	
	public static void auditDocDownload(ActivityType activityType, String contentQId,
			String contentIdentity, String docIdentity, Channel channel, 
			String contextName, String contextId, String contextTerm, String contentTitle) {
		
		ProcessContext pc = ProcessContext.get();
		auditDocActivity(activityType, contentQId, contentIdentity, docIdentity, channel, 
				new RegisteredActor(pc.getUserId(), pc.getUserDisplayName()), contextName, 
				contextId, contextTerm, contentTitle);
	}
	
	public static void auditDocActivity(ActivityType activityType, String contentQId, 
			String contentIdentity, String docIdentity, Channel channel, Actor actor,
			String contextName, String contextId, String contextTerm, String contentTitle) {
		
		ActivityAudit activity = new ActivityAudit();
		
		DocumentActivity docDownloadActvy = new DocumentActivity(activityType, contentIdentity, contentQId, 
				ContainerType.CONTENT, docIdentity, contextName, contextId, contextTerm, contentTitle);
		
		activity.setActivity(docDownloadActvy);
		activity.setActor(actor);
		
		activity.setChannel(new ActivityChannel(channel));
		activity.setActivityTime(Calendar.getInstance().getTime());
		
		auditActivity(activity);
	}

	public static void auditContentShare(String templateId, DisplayableContent content, 
			String sharedWith, ShareMedium sharedFrom, Channel channel, String sharingId, String contentTitle) {
		auditContentShareInternal(templateId, content.getRecordIdentity(), 
				content.getContainerQId(), sharedWith, sharedFrom, channel, sharingId, contentTitle);
	}
	
	public static void auditContentShareInternal(String templateId, 
			String contentIdentity, String containerQId, String sharedWith, 
			ShareMedium sharedFrom, Channel channel, String sharingId, String contentTitle) {
		
		ContentShareActivity shareActvy = new ContentShareActivity(contentIdentity, 
				containerQId, ContainerType.CONTENT, sharingId, sharedFrom, sharedWith, contentTitle);
		
		auditContentActivity(shareActvy, channel);
	}
	
	public static void auditContentShare(String templateId, List<ContentDataRef> contentList, 
			ShareMedium sharedFrom, Channel channel, String sharingId, String sharedWith, String contentTitle) {
		
		for (ContentDataRef content : contentList) {
			auditContentShareInternal(templateId, content.getInstanceIdentity(), 
					content.getContainerQId(), sharedWith, sharedFrom, channel, sharingId, content.getTitle());
		}
	}

	public static void auditContentAccess(ContentDataRef dataRef,
			ContainerType containerType, Channel channel) {
		
		ContentActivity contentActvy = new ContentAccessActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, dataRef.getTitle());
		
		auditContentActivity(contentActvy, channel, null);
	}
	
	public static void auditContentAccess(ContentDataRef dataRef,
			ContainerType containerType, Channel channel, String contextName, String contextId, String contextTerm) {
		
		ContentActivity contentActvy = new ContentAccessActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, contextName, contextId, contextTerm, dataRef.getTitle());
		
		auditContentActivity(contentActvy, channel, null);
	}

	public static void auditExternalLinkAccess(String url, String contentIdentity, 
			String contentItemQId, String aid, Channel channel) {
		
		Activity actvy = new ExternalLinkAccess(url, contentIdentity, contentItemQId);
		Actor actor = null;
		
		ProcessContext pc = ProcessContext.get();
		if (pc != null) {
			actor = new RegisteredActor(pc.getUserId(), pc.getUserDisplayName());
		} else if (!StringUtil.isEmpty(aid)) {
			actor = new NonRegisteredActor(aid);
		}
		
		auditActivity(actvy, actor, channel);
		
	}
	
	public static void auditContentConnActivity(ContentTypeConnection contentConn, ActivityType activityType) {
		
		ContentConnActivity activity = new ContentConnActivity(activityType);
		
		activity.setItemIdentity(contentConn.getIdentity());
		activity.setItemTitle(contentConn.getConnectionName());
		
		auditActivity(activity);
	}

	public static void auditContentExtLinkURLCopied(ContentDataRef dataRef,
			ContainerType containerType, Channel channel) {
		
		ContentActivity contentActvy = new ContentExtLinkURLCopy(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), containerType, dataRef.getTitle());
		
		auditContentActivity(contentActvy, channel);
	}

}
