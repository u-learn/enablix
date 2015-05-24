package com.enablix.analytics.recommendation.builder.web;

import com.enablix.analytics.recommendation.UserContext;
import com.enablix.analytics.recommendation.builder.UserContextBuilder;
import com.enablix.commons.util.process.ProcessContext;

public class WebUserContextBuilder implements UserContextBuilder<WebRecommendationRequest> {

	@Override
	public UserContext build(WebRecommendationRequest request) {
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
