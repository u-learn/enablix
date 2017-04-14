package com.enablix.app.content.event.listener;

import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.content.event.ContentDataDelEvent;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;

@Component
public class ContentSaveMQEventPublisher implements ContentDataEventListener {

	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		EventUtil.publishEvent(new Event<ContentDataSaveEvent>(Events.CONTENT_CHANGE_EVENT, event));
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
		// DO Nothing
	}

}
