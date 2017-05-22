package com.enablix.doc.preview.listener;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.mq.EventSubscription;
import com.enablix.doc.preview.DocPreviewService;

@Component
public class DocPreviewGenEventListener {

	@Autowired
	private DocPreviewService previewService;
	
	@Autowired
	private DocumentManager docManager;
	
	@SuppressWarnings("rawtypes")
	@EventSubscription(eventName = Events.CONTENT_CHANGE_EVENT)
	public void generatePreview(ContentDataSaveEvent event) throws IOException {
		
		for (ContentItemType contentItem : event.getContainerType().getContentItem()) {
		
			if (contentItem.getType() == ContentItemClassType.DOC) {
				
				Object fileMetadata = event.getDataAsMap().get(contentItem.getId());
				
				if (fileMetadata instanceof Map) {
				
					Map fileMdMap = (Map) fileMetadata;
					String docIdentity = (String) fileMdMap.get(ContentDataConstants.IDENTITY_KEY);
					
					DocumentMetadata docMetadata = docManager.getDocumentMetadata(docIdentity);
					if (docMetadata != null && docMetadata.getPreviewStatus() != PreviewStatus.AVAILABLE) {
						previewService.createPreview(docMetadata);				
					}
				}
			}
		}
		
	}
	
}
