package com.enablix.hubspot.model;

public class URIAction extends HubspotAction {

	public enum HttpMethod {
		GET, POST, PUT, DELETE
	}
	
	private HttpMethod httpMethod;
	
	private String uri;
	
	public URIAction(String type, String label, String uri) {
		this(type, label, uri, HttpMethod.GET);
	}
	
	public URIAction(String type, String label, String uri, HttpMethod httpMethod) {
		super(type, label);
		this.uri = uri;
		this.httpMethod = httpMethod;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
