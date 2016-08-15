package com.enablix.app.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.DocRef;

@Component
public class DocUnsecureAccessUrlPopulator {

	@Value("${site.url.doc.download}")
	private String docDownloadUrl;
	
	@Autowired
	private SharedContentUrlCreator urlCreator;
	
	public void process(DisplayableContent content, String sharedWithEmail) {
		
		DocRef contentDoc = content.getDoc();
		
		if (contentDoc != null) {
			String shareableUrl = urlCreator.createShareableUrl(
				getActualDocDownloadUrl(contentDoc.getDocIdentity()), sharedWithEmail, true);
			contentDoc.setAccessUrl(shareableUrl);
		}
		
	}
	
	private String getActualDocDownloadUrl(String docIdentity) {
		return docDownloadUrl.replaceAll(":docIdentity", docIdentity);
	}
	
}
