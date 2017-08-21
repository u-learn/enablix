package com.enablix.app.content.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
	
	public void populateUnsecureUrl(DisplayableContent content, String sharedWithEmail) {
		
		DocRef contentDoc = content.getDoc();
		
		if (contentDoc != null) {
			
			String shareableUrl = urlCreator.createShareableUrl(
					getActualDocDownloadUrl(contentDoc.getDocIdentity()), sharedWithEmail, true);
			contentDoc.setAccessUrl(EnvPropertiesUtil.getProperties().getServerUrl() + shareableUrl);
			
			String shareableThmbUrl = urlCreator.createShareableUrl(
					getActualDocThumbnailUrl(contentDoc.getDocIdentity()), sharedWithEmail, true);
			contentDoc.setThumbnailUrl(EnvPropertiesUtil.getProperties().getServerUrl() + shareableThmbUrl);
		}
		
	}
	
	public String getActualDocDownloadUrl(String docIdentity) {
		return docDownloadUrl.replaceAll(":docIdentity", docIdentity);
	}
	
	public String getActualDocThumbnailUrl(String docIdentity) {
		return docThumbnailUrl.replaceAll(":docIdentity", docIdentity);
	}
	
	public void populateSecureUrl(DisplayableContent content) {
		
		DocRef contentDoc = content.getDoc();
		
		if (contentDoc != null) {
			String shareableUrl = getActualDocDownloadUrl(contentDoc.getDocIdentity());
			contentDoc.setAccessUrl(EnvPropertiesUtil.getProperties().getServerUrl() + shareableUrl);
		}
		
	}

}
