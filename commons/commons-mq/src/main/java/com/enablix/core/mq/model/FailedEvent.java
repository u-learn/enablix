package com.enablix.core.mq.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;
import com.enablix.core.mq.Event;

@Document(collection = "ebx_failed_event")
public class FailedEvent extends BaseDocumentEntity {

	private Event<?> event;
	
	private String errorMessage;
	
	private String handlerId;

	public Event<?> getEvent() {
		return event;
	}

	public void setEvent(Event<?> event) {
		this.event = event;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}
	
}
