package com.enablix.app.content.event.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.content.event.ContentDataDelEvent;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.Actor;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.RegisteredActor;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;

@Component
public class ContentSaveAuditor implements ContentDataEventListener {

	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(event.getTemplateId());
		
		Map<String, Object> dataAsMap = event.getDataAsMap();
		
		ContentDataRef dataRef = ContentDataUtil.contentDataToRef(dataAsMap, 
				template, event.getContainerType().getQualifiedId());
		
		Actor actor = null;
		if (event.isNewRecord()) {
			actor = new RegisteredActor((String) dataAsMap.get(ContentDataConstants.CREATED_BY_KEY), 
					(String) dataAsMap.get(ContentDataConstants.CREATED_BY_NAME_KEY));
		} else {
			actor = new RegisteredActor((String) dataAsMap.get(ContentDataConstants.MODIFIED_BY_KEY), 
					(String) dataAsMap.get(ContentDataConstants.MODIFIED_BY_NAME_KEY));
		}
		
		ActivityLogger.auditContentActivity(
				event.isNewRecord() ? ActivityType.CONTENT_ADD : ActivityType.CONTENT_UPDATE, 
				dataRef, event.getContainerType().isRefData() ? ContainerType.REF_DATA : ContainerType.CONTENT, actor);
	}

	
	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		
		ContentDataRef dataRef = ContentDataRef.createContentRef(event.getTemplateId(), 
				event.getContainerQId(), event.getContentIdentity(), event.getContentTitle());
		
		ActivityLogger.auditContentActivity(
				ActivityType.CONTENT_DELETE, dataRef, 
				event.getContainerType().isRefData() ? ContainerType.REF_DATA : ContainerType.CONTENT, null);
	}

}
