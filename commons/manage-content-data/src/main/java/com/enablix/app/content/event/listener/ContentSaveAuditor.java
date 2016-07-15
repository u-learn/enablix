package com.enablix.app.content.event.listener;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataUtil;
import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ContentActivity;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.services.util.ActivityLogger;

@Component
public class ContentSaveAuditor implements ContentDataEventListener {

	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		
		ProcessContext processContext = ProcessContext.get();
		ContentDataRef dataRef = ContentDataUtil.contentDataToRef(event.getDataAsMap(), 
				event.getTemplateId(), event.getContainerType().getQualifiedId());
		
		ActivityAudit activity = new ActivityAudit();
		
		ContentActivity contentActvy = new ContentActivity(dataRef.getInstanceIdentity(), 
				dataRef.getContainerQId(), 
				event.getContainerType().isRefData() ? ContainerType.REF_DATA :ContainerType.CONTENT, 
				event.isNewRecord() ? ContentActivityType.CONTENT_ADD : ContentActivityType.CONTENT_UPDATE);
		
		activity.setActivity(contentActvy);
		
		RegisteredActor actor = new RegisteredActor();
		actor.setUserId(processContext.getUserId());
		activity.setActor(actor);
		
		activity.setChannel(new ActivityChannel(Channel.WEB));
		activity.setActivityTime(Calendar.getInstance().getTime());
		
		ActivityLogger.auditActivity(activity);
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		// TODO Auto-generated method stub
		
	}

}
