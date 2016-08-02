package com.enablix.app.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.id.UUIDIdentityGenerator;
import com.enablix.commons.util.process.ProcessContext;
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
		
		SharedSiteUrl sharedUrl = new SharedSiteUrl(sharedWithEmail, shareUrl, actualUrl, 
				ProcessContext.get().getTenantId(), ProcessContext.get().getTemplateId());
		
		sharedUrl = repo.save(sharedUrl);
		
		return sharedUrl.getSharedUrl();
	}

}
