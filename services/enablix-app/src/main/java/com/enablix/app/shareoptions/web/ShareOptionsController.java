package com.enablix.app.shareoptions.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.web.ActivityAuditController.ContentAuditRequest;
import com.enablix.commons.util.id.UUIDIdentityGenerator;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentActivity;
import com.enablix.core.domain.activity.ContentShareActivity.ShareMedium;
import com.enablix.services.util.ActivityLogger;


@RestController
@RequestMapping("shareOptions")
public class ShareOptionsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShareOptionsController.class);
	
	@Autowired
	private UUIDIdentityGenerator uuidGen;

	@RequestMapping(method = RequestMethod.POST, value = "/downldDocURLAudit")
	public @ResponseBody Boolean auditDownldDoc(@RequestBody ContentAuditRequest request) {
		try	{
			String templateId = ProcessContext.get().getTemplateId();
			ContentDataRef dataRef = new ContentDataRef(templateId, request.getContainerQId(), 
					request.getInstanceIdentity(), request.getItemTitle());
			ActivityLogger.auditContentDownldURLCopied(dataRef, ContentActivity.ContainerType.CONTENT, Channel.WEB);
			return true;
		}
		catch(Exception e) {
			LOGGER.error("Error Saving the Audit Data for the Activity :: Portal URL Copied",e);
			return false;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/portalURLAudit")
	public @ResponseBody Boolean auditPortalURL(@RequestBody ContentAuditRequest request) {
		try	{
			String templateId = ProcessContext.get().getTemplateId();
			ContentDataRef dataRef = new ContentDataRef(templateId, request.getContainerQId(), 
					request.getInstanceIdentity(), request.getItemTitle());
			ActivityLogger.auditContentPortalURLCopied(dataRef, ContentActivity.ContainerType.CONTENT, Channel.WEB);
			return true;
		}
		catch(Exception e) {
			LOGGER.error("Error Saving the Audit Data for the Activity :: Portal URL Copied",e);
			return false;
		}
	}

	@RequestMapping(value = "/auditShareViaEmailClient", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Boolean auditShareViaEmailClient(@RequestBody ContentAuditRequest request) {
		try	{
			String templateId = ProcessContext.get().getTemplateId();
			String uniqueShareId = uuidGen.generateId(null);
			ActivityLogger.auditContentShareInternal(templateId, request.getInstanceIdentity(),request.getContainerQId(),"",
					ShareMedium.WEB, Channel.EMAILCLIENT, uniqueShareId, request.getItemTitle());

			return true;
		}
		catch(Exception e)	{
			LOGGER.error("Error Saving the Audit Data for the Activity :: Portal URL Copied",e);
			return false;
		}
	}
	
}
