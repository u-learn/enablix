package com.enablix.content.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mq.EventSubscription;

@Component
public class HoldingContainerRefresher {

	@Autowired
	private ContentConnectionManager contentConnMgr;
	
	@EventSubscription(eventName = {Events.CONTENT_TEMPLATE_UPDATED})
	public void refreshHoldingContainer(ContentTemplate template) {
		contentConnMgr.refreshHoldingContainersForAllConnections();
	}
	
}
