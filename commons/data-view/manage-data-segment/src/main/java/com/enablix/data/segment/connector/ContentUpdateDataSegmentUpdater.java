package com.enablix.data.segment.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.core.mq.EventSubscription;
import com.enablix.data.segment.view.DataSegmentConnector;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;

@Component
public class ContentUpdateDataSegmentUpdater {

	@Autowired
	private DataSegmentConnectorRegistry connRegistry;
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	
	@EventSubscription(eventName = {Events.CONTENT_CHANGE_EVENT})
	public void updateDataSegment(ContentDataSaveEvent event) {
		
		
		ContentDataRecord record = new ContentDataRecord(event.getTemplateId(), 
				event.getContainerType().getQualifiedId(), event.getDataAsMap());
		
		DataSegmentInfo dsInfo = dsInfoBuilder.build(record);
		
		for (DataSegmentConnector<?> conn : connRegistry.getAllConnectors()) {
			conn.updateRecord(record, dsInfo);
		}
		
	}
	
}
