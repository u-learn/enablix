package com.enablix.analytics.recommendation.builder.web;

import com.enablix.analytics.recommendation.RequestContext;
import com.enablix.analytics.recommendation.builder.RequestContextBuilder;
import com.enablix.commons.util.process.ProcessContext;

public class WebRequestContextBuilder implements RequestContextBuilder<WebRecommendationRequest> {

	@Override
	public RequestContext build(WebRecommendationRequest request) {
		
		WebRequestContext webRequestContext = 
				new WebRequestContext(ProcessContext.get().getTemplateId());
		
		webRequestContext.setContainerQId(request.getContainerQId());
		webRequestContext.setContentIdentity(request.getContentIdentity());
		
		return webRequestContext;
	}

	public static class WebRequestContext implements RequestContext {

		private String templateId;
		
		private String containerQId;
		
		private String contentIdentity;
		
		public WebRequestContext(String templateId) {
			super();
			this.templateId = templateId;
		}

		@Override
		public String templateId() {
			return templateId;
		}

		public String containerQId() {
			return containerQId;
		}

		public void setContainerQId(String containerQId) {
			this.containerQId = containerQId;
		}

		@Override
		public String contentIdentity() {
			return contentIdentity;
		}

		public void setContentIdentity(String contentIdentity) {
			this.contentIdentity = contentIdentity;
		}
		
	}
	
}
