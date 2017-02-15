package com.enablix.app.content.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class ContentSaveAuditor implements ContentDataEventListener {

	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		
		TemplateWrapper template = templateMgr.getTemplateWrapper(event.getTemplateId());
		ContentDataRef dataRef = ContentDataUtil.contentDataToRef(event.getDataAsMap(), 
				template, event.getContainerType().getQualifiedId());
		
		ActivityLogger.auditContentActivity(
				event.isNewRecord() ? ContentActivityType.CONTENT_ADD : ContentActivityType.CONTENT_UPDATE, 
				dataRef, event.getContainerType().isRefData() ? ContainerType.REF_DATA : ContainerType.CONTENT);
	}

	
	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		
		ContentDataRef dataRef = ContentDataRef.createContentRef(event.getTemplateId(), 
				event.getContainerQId(), event.getContentIdentity(), event.getContentTitle());
		
		ActivityLogger.auditContentActivity(
				ContentActivityType.CONTENT_DELETE, 
				dataRef, event.getContainerType().isRefData() ? ContainerType.REF_DATA : ContainerType.CONTENT);
	}

}
