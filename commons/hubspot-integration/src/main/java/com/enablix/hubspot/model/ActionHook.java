package com.enablix.hubspot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ActionHook extends URIAction {

	private static final String ACTION_HOOK = "ACTION_HOOK";
	
	public ActionHook(String label, String uri) {
		super(ACTION_HOOK, label, uri);
	}
	
	public ActionHook(String label, String uri, HttpMethod httpMethod) {
		super(ACTION_HOOK, label, uri, httpMethod);
	}

}
