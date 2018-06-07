package com.enablix.app.content.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.DocRef;

@Component
public class DocUnsecureAccessUrlPopulator {

	@Value("${site.url.doc.download}")
	private String docDownloadUrl;
	
	@Value("${site.url.doc.thumbnail}")
	private String docThumbnailUrl;
	
	@Autowired
	private SharedContentUrlCreator urlCreator;
	
	public void populateUnsecureUrl(DisplayableContent content, String sharedWithEmail, DisplayContext ctx) {
		
		DocRef contentDoc = content.getDoc();
		
		if (contentDoc != null) {
			
			String shareableUrl = urlCreator.createShareableUrl(
					getActualDocDownloadUrl(contentDoc.getDocIdentity(), ctx), sharedWithEmail, true);
			contentDoc.setAccessUrl(EnvPropertiesUtil.getSubdomainSpecificServerUrl() + shareableUrl);
			
			String shareableThmbUrl = urlCreator.createShareableUrl(
					getActualDocThumbnailUrl(contentDoc.getDocIdentity(), ctx), sharedWithEmail, true);
			contentDoc.setThumbnailUrl(EnvPropertiesUtil.getSubdomainSpecificServerUrl() + shareableThmbUrl);
		}
		
	}
	
	public String getActualDocDownloadUrl(String docIdentity, DisplayContext ctx) {
		return ctx.appendTrackingParams(docDownloadUrl.replaceAll(":docIdentity", docIdentity));
	}
	
	public String getActualDocThumbnailUrl(String docIdentity, DisplayContext ctx) {
		return ctx.appendTrackingParams(docThumbnailUrl.replaceAll(":docIdentity", docIdentity));
	}
	
	public void populateSecureUrl(DisplayableContent content, DisplayContext ctx) {
		
		DocRef contentDoc = content.getDoc();
		
		if (contentDoc != null) {
			String shareableUrl = getActualDocDownloadUrl(contentDoc.getDocIdentity(), ctx);
			contentDoc.setAccessUrl(EnvPropertiesUtil.getSubdomainSpecificServerUrl() + shareableUrl);
		}
		
	}

}
