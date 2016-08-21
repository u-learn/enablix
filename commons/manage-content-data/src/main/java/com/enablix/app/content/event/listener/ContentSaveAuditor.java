package com.enablix.app.content.event.listener;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataUtil;
import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;
import com.enablix.services.util.ActivityLogger;

@Component
public class ContentSaveAuditor implements ContentDataEventListener {

	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		
		ContentDataRef dataRef = ContentDataUtil.contentDataToRef(event.getDataAsMap(), 
				event.getTemplateId(), event.getContainerType().getQualifiedId());
		
		ActivityLogger.auditContentActivity(
				event.isNewRecord() ? ContentActivityType.CONTENT_ADD : ContentActivityType.CONTENT_UPDATE, 
				dataRef, event.getContainerType().isRefData() ? ContainerType.REF_DATA : ContainerType.CONTENT);
	}

	
	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		
		ContentDataRef dataRef = new ContentDataRef(event.getTemplateId(), 
				event.getContainerQId(), event.getContentIdentity());
		
		ActivityLogger.auditContentActivity(
				ContentActivityType.CONTENT_DELETE, 
				dataRef, event.getContainerType().isRefData() ? ContainerType.REF_DATA : ContainerType.CONTENT);
	}

}
