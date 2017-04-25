package com.enablix.analytics.web.request;

import com.enablix.analytics.context.UserContext;
import com.enablix.analytics.context.builder.UserContextBuilder;
import com.enablix.commons.util.process.ProcessContext;

public class WebUserContextBuilder<T extends WebContentRequest> implements UserContextBuilder<T> {

	@Override
	public UserContext build(T request) {
		return new WebUserContext(ProcessContext.get().getUserId(), ProcessContext.get().getClientId());
	}
	
	private static class WebUserContext implements UserContext {

		private String userId;
		
		private String clientId;
		
		public WebUserContext(String userId, String clientId) {
			super();
			this.userId = userId;
			this.clientId = clientId;
		}

		@Override
		public String userId() {
			return userId;
		}

		@Override
		public String clientId() {
			return clientId;
		}
		
	}

}
