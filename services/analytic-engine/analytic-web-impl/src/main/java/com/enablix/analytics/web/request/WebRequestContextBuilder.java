package com.enablix.analytics.web.request;

import com.enablix.analytics.context.RequestContext;
import com.enablix.analytics.context.builder.RequestContextBuilder;
import com.enablix.commons.util.process.ProcessContext;

public class WebRequestContextBuilder<T extends WebContentRequest> implements RequestContextBuilder<T> {

	@Override
	public RequestContext build(T request) {
		
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
