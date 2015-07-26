package com.enablix.analytics.web.request;

import com.enablix.analytics.context.UserContext;
import com.enablix.analytics.context.builder.UserContextBuilder;
import com.enablix.commons.util.process.ProcessContext;

public class WebUserContextBuilder<T extends WebContentRequest> implements UserContextBuilder<T> {

	@Override
	public UserContext build(T request) {
		return new WebUserContext(ProcessContext.get().getUserId());
	}
	
	private static class WebUserContext implements UserContext {

		private String userId;
		
		public WebUserContext(String userId) {
			super();
			this.userId = userId;
		}

		@Override
		public String userId() {
			return userId;
		}
		
	}

}
