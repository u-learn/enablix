package com.enablix.app.content.ui.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentActivity;
import com.enablix.services.util.ActivityLogger;

@RestController
@RequestMapping("activity")
public class ActivityAuditController {
	
	@RequestMapping(method = RequestMethod.POST, value="/content/access/")
	public void auditContentAccess(@RequestBody ContentAuditRequest request) {
		String templateId = ProcessContext.get().getTemplateId();
		ContentDataRef dataRef = new ContentDataRef(templateId, request.getContainerQId(), request.getInstanceIdentity());
		ActivityLogger.auditContentAccess(dataRef, ContentActivity.ContainerType.CONTENT, Channel.WEB);
	}
	
	public static class ContentAuditRequest {
		
		private String containerQId;
		private String instanceIdentity;
		
		public String getContainerQId() {
			return containerQId;
		}
		
		public void setContainerQId(String containerQId) {
			this.containerQId = containerQId;
		}
		
		public String getInstanceIdentity() {
			return instanceIdentity;
		}
		
		public void setInstanceIdentity(String instanceIdentity) {
			this.instanceIdentity = instanceIdentity;
		}
		
	}
}
