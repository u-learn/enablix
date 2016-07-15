package com.enablix.trigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.ItemUserCorrelationService;
import com.enablix.app.content.ContentDataUtil;
import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.ContentChange.TriggerType;
import com.enablix.core.mq.EventSubscription;
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
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		// Do Nothing
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		ContentDataRef item = new ContentDataRef(event.getTemplateId(), event.getContainerQId(), event.getContentIdentity());
		itemItemCorrService.deleteCorrelationsForItem(item);
		itemUserCorrService.deleteCorrelationsForItem(item);
	}
	
	@EventSubscription(eventName = {Events.CONTENT_CHANGE_EVENT})
	public void handleContentChangeEvent(ContentDataSaveEvent event) {
		
		ContentChange contentChangeTrigger = new ContentChange();
		
		ContentDataRef triggerItem = ContentDataUtil.contentDataToRef(
				event.getDataAsMap(), event.getTemplateId(), event.getContainerType().getQualifiedId());
		
		contentChangeTrigger.setTriggerItem(triggerItem);
		contentChangeTrigger.setType(event.isNewRecord() ? TriggerType.ADD : TriggerType.UPDATE);
		
		TriggerLifecycleManager<ContentChange> triggerLifecycleManager = 
				lifecycleMgrFactory.getTriggerLifecycleManager(contentChangeTrigger);
		
		triggerLifecycleManager.startLifecycle(contentChangeTrigger);
	}

}
