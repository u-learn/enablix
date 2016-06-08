package com.enablix.core.mq;

import java.util.HashMap;
import java.util.Map;

public class Event<T> {

	private T payload;
	
	private String name;
	
	private Map<String, Object> headers;

	public Event(String name, T payload) {
		super();
		this.payload = payload;
		this.name = name;
		this.headers = new HashMap<>();
	}

	public T getPayload() {
		return payload;
	}

	public String getName() {
		return name;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}
	
	public void addHeader(String headerName, Object headerValue) {
		headers.put(headerName, headerValue);
	}
	
}
