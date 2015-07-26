package com.enablix.analytics.recommendation.builder.impl;

import com.enablix.analytics.context.RequestContext;
import com.enablix.analytics.context.UserContext;
import com.enablix.analytics.context.builder.RequestContextBuilder;
import com.enablix.analytics.context.builder.UserContextBuilder;
import com.enablix.analytics.recommendation.RecommendationContext;
import com.enablix.analytics.recommendation.RecommendationRequest;
import com.enablix.analytics.recommendation.builder.RecommendationContextBuilder;

public class SimpleRecommendationContextBuilder<R extends RecommendationRequest> 
		implements RecommendationContextBuilder<R> {

	private UserContextBuilder<R> ucBuilder;
	
	private RequestContextBuilder<R> rcBuilder;

	public SimpleRecommendationContextBuilder(
			UserContextBuilder<R> ucBuilder, RequestContextBuilder<R> rcBuilder) {
		super();
		this.ucBuilder = ucBuilder;
		this.rcBuilder = rcBuilder;
	}

	@Override
	public RecommendationContext build(R request) {
		return new SimpleRecommendationContext(
				ucBuilder.build(request), rcBuilder.build(request));
	}

	
	private static class SimpleRecommendationContext implements RecommendationContext {
		
		private UserContext userContext;
		private RequestContext requestContext;
		
		public SimpleRecommendationContext(UserContext uc, RequestContext rc) {
			super();
			this.userContext = uc;
			this.requestContext = rc;
		}

		@Override
		public UserContext getUserContext() {
			return userContext;
		}

		@Override
		public RequestContext getRequestContext() {
			return requestContext;
		}
		
	}
	
	
}
