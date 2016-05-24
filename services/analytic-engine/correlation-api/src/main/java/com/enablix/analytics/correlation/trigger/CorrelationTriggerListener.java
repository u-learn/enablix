package com.enablix.analytics.correlation.trigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.task.ContentChange;
import com.enablix.analytics.task.ContentChange.TriggerType;
import com.enablix.analytics.task.TaskChain;
import com.enablix.analytics.task.TriggerTaskFactory;
import com.enablix.app.content.ContentDataUtil;
import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.core.api.ContentDataRef;

@Component
public class CorrelationTriggerListener implements ContentDataEventListener {

	@Autowired
	private TriggerTaskFactory taskFactory;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		
		ContentChange contentChangeTrigger = new ContentChange();
		
		ContentDataRef triggerItem = ContentDataUtil.contentDataToRef(
				event.getDataAsMap(), event.getTemplateId(), event.getContainerType().getQualifiedId());
		
		contentChangeTrigger.setTriggerItem(triggerItem);
		contentChangeTrigger.setType(event.isNewRecord() ? TriggerType.ADD : TriggerType.UPDATE);
		
		TaskChain<ContentChange> taskChain = taskFactory.getTaskChain(contentChangeTrigger);
		if (taskChain != null) {
			taskChain.doTask(contentChangeTrigger);
		}
		
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		// TODO Auto-generated method stub
		
	}

}
