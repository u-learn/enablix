package com.enablix.app.content.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.id.UUIDIdentityGenerator;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.activity.audit.ActivityTrackingConstants;
import com.enablix.core.domain.share.SharedSiteUrl;
import com.enablix.core.system.repo.SharedSiteUrlRepository;

@Component
public class SharedContentUrlCreatorImpl implements SharedContentUrlCreator {

	@Autowired
	private SharedSiteUrlRepository repo;
	
	@Autowired
	private UUIDIdentityGenerator uuidGen;
	
	@Override
	public String createShareableUrl(String actualUrl, String sharedWithEmail, boolean allowUnAuthAccess) {
		
		String uniqueAccessId = uuidGen.generateId(null);
		String shareUrl = (allowUnAuthAccess ? ShareContentConstants.UNSECURE_SHARE_URL_PREFIX 
				: ShareContentConstants.SECURE_SHARE_URL_PREFIX) + "/" + uniqueAccessId; 
		
		actualUrl = appendAccessId(actualUrl, uniqueAccessId);
		
		SharedSiteUrl sharedUrl = new SharedSiteUrl(sharedWithEmail, shareUrl, actualUrl, 
				ProcessContext.get().getTenantId(), ProcessContext.get().getTemplateId());
		
		sharedUrl = repo.save(sharedUrl);
		
		return sharedUrl.getSharedUrl();
	}

	private String appendAccessId(String actualUrl, String accessId) {
		
		if (!actualUrl.contains(ActivityTrackingConstants.CONTEXT_ID)) {
			
			StringBuilder sb = new StringBuilder(actualUrl.length() + 52);
			sb.append(actualUrl);
			
			if (!actualUrl.contains("?")) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			
			actualUrl =  sb.append(ActivityTrackingConstants.CONTEXT_ID).append("=").append(accessId).toString();
		}
		
		return actualUrl;
	}
	
	public static void main(String[] args) {
		String actualUrl = "/doc/download/45b9ad67-14bd-49ad-a734-5cfd89e1f861?atChannel=WEB&atContext=COPYNSHARE";
		System.out.println(new SharedContentUrlCreatorImpl().appendAccessId(actualUrl, "12314"));
	}

}
