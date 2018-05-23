package com.enablix.app.shareoptions.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.share.SharedContentUrlCreator;
import com.enablix.app.content.ui.web.ActivityAuditController.ContentAuditRequest;
import com.enablix.commons.util.EnvPropertiesUtil;
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
	
	@Autowired
	private SharedContentUrlCreator shareableUrlCreator;
	
	@Value("${site.url.content.widget}")
	private String contentWidgetUrl;
	
	@Value("${site.content.widget.embed.code}")
	private String contentWidgetEmbedCode;

	@RequestMapping(method = RequestMethod.POST, value = "/downldDocURLCopyAudit/")
	public @ResponseBody Boolean auditDocUrlCopy(@RequestBody ContentAuditRequest request) {
		
		try	{
			
			String templateId = ProcessContext.get().getTemplateId();
			ContentDataRef dataRef = ContentDataRef.createContentRef(templateId, request.getContainerQId(), 
					request.getInstanceIdentity(), request.getItemTitle());
			
			ActivityLogger.auditContentDownldURLCopied(dataRef, ContentActivity.ContainerType.CONTENT, Channel.WEB);
			
			return true;
			
		} catch(Exception e) {
			LOGGER.error("Error Saving the Audit Data for the Activity :: Doc Download URL Copied",e);
			return false;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/shareableDocUrl/", produces="text/plain")
	public @ResponseBody String getShareableDocUrl(@RequestBody ShareableUrlRequest request) {
		
		String serverUrl = EnvPropertiesUtil.getProperties().getServerUrl();
		String userId = ProcessContext.get().getUserId();
		
		String shareableUrl = shareableUrlCreator.createShareableUrl(request.getUrl(), userId, true);
		
		auditDocUrlCopy(request.getAuditRequest());
		
		return serverUrl + shareableUrl;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/shareableDocUrlv2/", produces="application/json")
	public ShareableUrlResponse getShareableDocUrlV2(@RequestBody ShareableUrlRequest request) {
		return new ShareableUrlResponse(getShareableDocUrl(request));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/shareableLinkUrl/", produces="application/json")
	public ShareableUrlResponse getShareableLinkUrl(@RequestBody ShareableUrlRequest request) {
		
		String serverUrl = EnvPropertiesUtil.getProperties().getServerUrl();
		String userId = ProcessContext.get().getUserId();
		
		String shareableUrl = shareableUrlCreator.createShareableUrl(request.getUrl(), userId, true);
		
		auditExtLinkUrlCopy(request.getAuditRequest());
		
		return new ShareableUrlResponse(serverUrl + shareableUrl);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/shareableContentWidgetUrl/", produces="application/json")
	public ShareableUrlResponse getShareableContentWidgetUrl(@RequestBody ShareableUrlRequest request) {
		
		String serverUrl = EnvPropertiesUtil.getProperties().getServerUrl();
		String userId = ProcessContext.get().getUserId();
		
		String shareableUrl = shareableUrlCreator.createShareableUrl(request.getUrl(), userId, true);
		
		String id = shareableUrl.substring(shareableUrl.lastIndexOf('/') + 1);
		String widgetUrl = contentWidgetUrl.replaceFirst(":accessId", id);
		
		String embedCode = contentWidgetEmbedCode.replaceFirst(":contentwidgeturl", (serverUrl + widgetUrl));
		
		return new ShareableUrlResponse(embedCode);
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "/extLinkURLCopyAudit/")
	public @ResponseBody Boolean auditExtLinkUrlCopy(@RequestBody ContentAuditRequest request) {
		
		try	{
		
			String templateId = ProcessContext.get().getTemplateId();
			
			ContentDataRef dataRef = ContentDataRef.createContentRef(templateId, request.getContainerQId(), 
					request.getInstanceIdentity(), request.getItemTitle());
			
			ActivityLogger.auditContentExtLinkURLCopied(dataRef, ContentActivity.ContainerType.CONTENT, Channel.WEB);
			
			return true;
			
		} catch(Exception e) {
			LOGGER.error("Error Saving the Audit Data for the Activity :: Portal URL Copied",e);
			return false;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/portalURLCopyAudit/")
	public @ResponseBody Boolean auditPortalURLCopy(@RequestBody ContentAuditRequest request) {
		try	{
			String templateId = ProcessContext.get().getTemplateId();
			ContentDataRef dataRef = ContentDataRef.createContentRef(templateId, request.getContainerQId(), 
					request.getInstanceIdentity(), request.getItemTitle());
			ActivityLogger.auditContentPortalURLCopied(dataRef, ContentActivity.ContainerType.CONTENT, Channel.WEB);
			return true;
		}
		catch(Exception e) {
			LOGGER.error("Error Saving the Audit Data for the Activity :: Portal URL Copied",e);
			return false;
		}
	}

	@RequestMapping(value = "/auditShareViaEmailClient/", method = RequestMethod.POST, produces = "application/json")
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
	
	public static class ShareableUrlRequest {
		
		private String url;
		
		private ContentAuditRequest auditRequest;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public ContentAuditRequest getAuditRequest() {
			return auditRequest;
		}

		public void setAuditRequest(ContentAuditRequest auditRequest) {
			this.auditRequest = auditRequest;
		}
		
	}
	
	public static class ShareableUrlResponse {
		
		private String shareableUrl;
		
		public ShareableUrlResponse() { }

		public ShareableUrlResponse(String shareableUrl) {
			this.shareableUrl = shareableUrl;
		}

		public String getShareableUrl() {
			return shareableUrl;
		}

		public void setShareableUrl(String shareableUrl) {
			this.shareableUrl = shareableUrl;
		}
		
	}
	
}
