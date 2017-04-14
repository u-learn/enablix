package com.enablix.trigger;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.ItemUserCorrelationService;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.content.event.ContentDataDelEvent;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.ContentChange.TriggerType;
import com.enablix.core.mq.EventSubscription;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.trigger.lifecycle.TriggerLifecycleManager;
import com.enablix.trigger.lifecycle.TriggerLifecycleManagerFactory;

@Component
public class ContentTriggerListener implements ContentDataEventListener {

	@Autowired
	private TriggerLifecycleManagerFactory lifecycleMgrFactory;
	
	@Autowired
	private ItemUserCorrelationService itemUserCorrService;
	
	@Autowired
	private ItemCorrelationService itemItemCorrService;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		// Do Nothing
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		ContentDataRef item = ContentDataRef.createContentRef(event.getTemplateId(), 
				event.getContainerQId(), event.getContentIdentity(), event.getContentTitle());
		itemItemCorrService.deleteCorrelationsForItem(item);
		itemUserCorrService.deleteCorrelationsForItem(item);
	}
	
	@EventSubscription(eventName = {Events.CONTENT_CHANGE_EVENT})
	public void handleContentChangeEvent(ContentDataSaveEvent event) {
		
		Date contentTriggerDate = event.isNewRecord() ? ContentDataUtil.getContentCreatedAt(event.getDataAsMap())
				: ContentDataUtil.getContentModifiedAt(event.getDataAsMap());
		
		if (contentTriggerDate == null) {
			contentTriggerDate = Calendar.getInstance().getTime();
		}
		
		TemplateFacade template = templateMgr.getTemplateFacade(event.getTemplateId());
		ContentChange contentChangeTrigger = new ContentChange(contentTriggerDate);
		
		contentChangeTrigger.setContentChange(event.getChangeDelta());
		
		ContentDataRef triggerItem = ContentDataUtil.contentDataToRef(
				event.getDataAsMap(), template, event.getContainerType().getQualifiedId());
		
		contentChangeTrigger.setTriggerItem(triggerItem);
		contentChangeTrigger.setType(event.isNewRecord() ? TriggerType.ADD : TriggerType.UPDATE);
		
		TriggerLifecycleManager<ContentChange> triggerLifecycleManager = 
				lifecycleMgrFactory.getTriggerLifecycleManager(contentChangeTrigger);
		
		triggerLifecycleManager.startLifecycle(contentChangeTrigger);
	}

}
