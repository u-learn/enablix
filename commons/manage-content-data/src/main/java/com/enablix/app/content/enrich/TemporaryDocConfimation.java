package com.enablix.app.content.enrich;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class TemporaryDocConfimation implements ContentEnricher {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemporaryDocConfimation.class);
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private AuditInfoDateFormatter dateFormatter;
	
	
	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, TemplateFacade contentTemplate) {
		
		String parentIdentity = updateCtx.parentIdentity();
		String contentQId = updateCtx.contentQId();
		String contentIdentity = (String) content.get(ContentDataConstants.IDENTITY_KEY);
		
		ContainerType containerDef = contentTemplate.getContainerDefinition(contentQId);
		
		for (ContentItemType itemDef : containerDef.getContentItem()) {

			if (itemDef.getType() == ContentItemClassType.DOC) {
			
				processDoc(content, parentIdentity, contentQId, contentIdentity, itemDef.getId(), false);
			}
		}
		
		processDoc(content, parentIdentity, contentQId, contentIdentity, ContentDataConstants.THUMBNAIL_DOC_FLD, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processDoc(Map<String, Object> content, String parentIdentity, String contentQId,
			String contentIdentity, String docFldId, boolean thumbnailDoc) {
		
		Object object = content.get(docFldId);
		String docContentQId = QIdUtil.createQualifiedId(contentQId, docFldId);
		
		if (object != null && object instanceof Map) {
		
			Map docMap = (Map) object;
			Object tempDoc = docMap.get(ContentDataConstants.DOC_TEMPORARY_ATTR);
			Object delDoc = docMap.get(ContentDataConstants.DOC_DELETED_ATTR);
			String previewStatus = (String) docMap.get(ContentDataConstants.DOC_PREVIEW_STATUS_ATTR);
			
			boolean deleteDoc = delDoc != null && delDoc instanceof Boolean && ((Boolean) delDoc);
			boolean tempDocBool = tempDoc != null && tempDoc instanceof Boolean && ((Boolean) tempDoc);
			boolean psPending = PreviewStatus.PENDING.equals(previewStatus);
			
			if (tempDocBool || psPending || deleteDoc) {
				
				String docIdentity = (String) docMap.get(ContentDataConstants.IDENTITY_KEY);
				DocumentMetadata docMd = docManager.getDocumentMetadata(docIdentity);
				
				if (deleteDoc) {
					
					try {
						
						docManager.delete(docMd);
						content.put(docFldId, null);
						
					} catch (IOException e) {
						LOGGER.debug("Failed to delete document", e);
						throw new RuntimeException("Unable to delete document", e);
					}
					
				} else if (tempDocBool) {
					
					docMd.setContentQId(docContentQId);
					
					try {
						
						if (!StringUtil.isEmpty(parentIdentity)) {
							docMd = docManager.attachUsingContainerInfo(docMd, contentQId, parentIdentity, thumbnailDoc);
						} else {
							docMd = docManager.attachUsingContainerInfo(docMd, contentQId, contentIdentity, thumbnailDoc);
						}
						
						Map<?, ?> beanToMap = BeanUtil.beanToMap(docMd);
						dateFormatter.formatAuditDates(beanToMap);
						content.put(docFldId, beanToMap);
						
					} catch (IOException e) {
						LOGGER.debug("Unable to attach document", e);
						throw new RuntimeException("Unable to attach document", e);
					}
					
				} else if (psPending) {
					docMap.put(ContentDataConstants.DOC_PREVIEW_STATUS_ATTR, docMd.getPreviewStatus().toString());
				}
			}
		}
	}

}
