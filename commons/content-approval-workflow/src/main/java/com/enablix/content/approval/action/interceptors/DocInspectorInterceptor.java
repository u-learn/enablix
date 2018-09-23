package com.enablix.content.approval.action.interceptors;

import static com.enablix.content.approval.ContentApprovalConstants.ACTION_EDIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_SUBMIT;
import static com.enablix.content.approval.ContentApprovalConstants.ACTION_WITHDRAW;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.state.change.impl.ActionInterceptorAdapter;
import com.enablix.state.change.model.ActionInput;

@Component
public class DocInspectorInterceptor extends ActionInterceptorAdapter<ContentDetail, ContentApproval> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocInspectorInterceptor.class);
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Override
	public void onActionStart(String actionName, ActionInput actionIn, ContentApproval recording) {
		if (actionIn instanceof ContentDetail) {
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			ContentDetail detail = (ContentDetail) actionIn;
			preProcessDoc(detail.getContentQId(), detail.getData(), template);
		}
	}
	
	@Override
	public void onActionComplete(String actionName, ActionInput actionIn, ContentApproval recording) {
		if (actionIn instanceof ContentDetail) {
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
			ContentDetail detail = (ContentDetail) actionIn;
			generatePreview(detail.getContentQId(), detail.getData(), template);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void preProcessDoc(String contentQId, Map<String, Object> content, TemplateFacade template) {
		
		ContainerType containerDef = template.getContainerDefinition(contentQId);
		
		for (ContentItemType itemDef : containerDef.getContentItem()) {

			if (itemDef.getType() == ContentItemClassType.DOC) {
			
				Object object = content.get(itemDef.getId());
				
				if (object != null && object instanceof Map) {
				
					Map docMap = (Map) object;
					Object delDoc = docMap.get(ContentDataConstants.DOC_DELETED_ATTR);
					
					boolean deleteDoc = delDoc != null && delDoc instanceof Boolean && ((Boolean) delDoc);
					
					if (deleteDoc) {
						
						String docIdentity = (String) docMap.get(ContentDataConstants.IDENTITY_KEY);
						DocumentMetadata docMd = docManager.getDocumentMetadata(docIdentity);
						
						try {
							
							docManager.delete(docMd);
							content.put(itemDef.getId(), null);
							
						} catch (IOException e) {
							LOGGER.debug("Failed to delete document", e);
							throw new RuntimeException("Unable to delete document", e);
						}
							
					} else {
						
						String docContentQId = (String) docMap.get(ContentDataConstants.CONTENT_QID_KEY);
						
						if (!itemDef.getQualifiedId().equals(docContentQId)) {
						
							docMap.put(ContentDataConstants.CONTENT_QID_KEY, itemDef.getQualifiedId());
							
							String docIdentity = (String) docMap.get(ContentDataConstants.IDENTITY_KEY);
							docManager.updateContentQId(docIdentity, itemDef.getQualifiedId());
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void generatePreview(String contentQId, Map<String, Object> content, TemplateFacade template) {
		
		ContainerType containerDef = template.getContainerDefinition(contentQId);
		
		for (ContentItemType itemDef : containerDef.getContentItem()) {

			if (itemDef.getType() == ContentItemClassType.DOC) {
			
				Object object = content.get(itemDef.getId());
				
				if (object != null && object instanceof Map) {
				
					Map docMap = (Map) object;
					Object previewStatus = docMap.get(ContentDataConstants.DOC_PREVIEW_STATUS_ATTR);
					
					if (PreviewStatus.PENDING.name().equals(previewStatus)) {
						
						String docIdentity = (String) docMap.get(ContentDataConstants.IDENTITY_KEY);
						DocumentMetadata docMd = docManager.getDocumentMetadata(docIdentity);
							
						EventUtil.publishEvent(new Event<DocumentMetadata>(Events.GENERATE_DOC_PREVIEW, docMd));
					}
				}
			}
		}
	}

	@Override
	public String[] actionsInterestedIn() {
		return new String[] { ACTION_EDIT, ACTION_SUBMIT, ACTION_WITHDRAW };
	}

	@Override
	public String workflowName() {
		return ContentApprovalConstants.WORKFLOW_NAME;
	}

}
