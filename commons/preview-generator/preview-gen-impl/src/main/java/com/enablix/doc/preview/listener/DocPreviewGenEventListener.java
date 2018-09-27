package com.enablix.doc.preview.listener;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.content.event.ContentDataSaveEvent;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mq.EventSubscription;
import com.enablix.doc.preview.DocPreviewService;

@Component
public class DocPreviewGenEventListener {

	@Autowired
	private DocPreviewService previewService;
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao genericDao;
	
	@EventSubscription(eventName = Events.CONTENT_CHANGE_EVENT)
	public void generatePreview(ContentDataSaveEvent event) throws IOException {
		
		for (ContentItemType contentItem : event.getContainerType().getContentItem()) {
			if (contentItem.getType() == ContentItemClassType.DOC) {
				checkAndGeneratePreview(event, contentItem.getId());
			}
		}
		
		checkAndGeneratePreview(event, ContentDataConstants.THUMBNAIL_DOC_FLD);
		
	}

	@SuppressWarnings("rawtypes")
	private void checkAndGeneratePreview(ContentDataSaveEvent event, String docFldId) throws IOException {
		
		Object fileMetadata = event.getDataAsMap().get(docFldId);
		
		if (fileMetadata instanceof Map) {
		
			Map fileMdMap = (Map) fileMetadata;
			
			String previewStatus = (String) fileMdMap.get("previewStatus");
			String docIdentity = (String) fileMdMap.get(ContentDataConstants.IDENTITY_KEY);
			
			DocumentMetadata docMetadata = docManager.getDocumentMetadata(docIdentity);
			
			if (docMetadata != null) {
				
				if (docMetadata.getPreviewStatus() != PreviewStatus.AVAILABLE
					&& docMetadata.getPreviewStatus() != PreviewStatus.NOT_SUPPORTED) {
					
					previewService.createPreview(docMetadata);
					
				} else if (PreviewStatus.PENDING.equals(previewStatus)) {
					// update status in content record
					updateStatusInRecord(docMetadata);
				}
			} 
		}
	}
	
	@EventSubscription(eventName = Events.GENERATE_DOC_PREVIEW)
	public void generateDocPreview(DocumentMetadata docMetadata) throws IOException {
		
		if (docMetadata != null) {
			
			if (docMetadata.getPreviewStatus() != PreviewStatus.AVAILABLE
				&& docMetadata.getPreviewStatus() != PreviewStatus.NOT_SUPPORTED) {
				
				previewService.createPreview(docMetadata);
				
			}
		} 
	}
	
	private void updateStatusInRecord(DocumentMetadata docMetadata) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		// update in content collection as well
		String docIdentity = docMetadata.getIdentity();
		String docQId = docMetadata.getContentQId();
		String parentQId = QIdUtil.getParentQId(docQId);
		String parentCollName = template.getCollectionName(parentQId);
		
		if (StringUtil.hasText(parentCollName)) {
		
			String docAttrId = QIdUtil.getElementId(docQId);
			String docIdentityAttr = docAttrId + "." + ContentDataConstants.IDENTITY_KEY;
			String previewStatusAttr = docAttrId + "." + DocumentMetadata.PREVIEW_STATUS_FLD_ID;
			
			StringFilter docIdentitFilter = new StringFilter(docIdentityAttr, docIdentity, ConditionOperator.EQ);
			Query query = new Query(docIdentitFilter.toPredicate(new Criteria()));

			Update update = new Update();
			update.set(previewStatusAttr, docMetadata.getPreviewStatus());

			genericDao.updateMulti(query, update, parentCollName);
		}
	}
	
}
