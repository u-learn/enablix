package com.enablix.app.content.recent;

import com.enablix.analytics.context.RequestContext;

public class RecentContentContext {

	private RequestContext requestContext;
	
	public RecentContentContext(RequestContext ctx) {
		this.requestContext = ctx;
	}

	public RequestContext getRequestContext() {
		return requestContext;
	}

	public void setRequestContext(RequestContext requestContext) {
		this.requestContext = requestContext;
	}
	
}
