package com.enablix.app.content.recent;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.analytics.web.request.WebRequestContextBuilder;

public class RecentContentWebRequestBuilder implements RecentContentContextBuilder<WebContentRequest> {

	private WebRequestContextBuilder<WebContentRequest> requestCtxBuilder = 
			new WebRequestContextBuilder<WebContentRequest>();
	
	@Override
	public RecentContentContext build(WebContentRequest request) {
		return new RecentContentContext(requestCtxBuilder.build(request));
	}

}
