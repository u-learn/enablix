package com.enablix.analytics.info.detection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentRecord;
import com.enablix.core.api.DocAttachment;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.StateChangeWorkflowManager;
import com.enablix.state.change.model.StateChangeRecording;

public class ContentRecordPersister {

	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private StateChangeWorkflowManager wfManager;
	
	public List<ContentSuggestion> persist(List<ContentSuggestion> suggestions, 
			@Header("saveAsDraft") Boolean saveAsDraft) throws IOException, ActionException {
		
		List<ContentSuggestion> savedSuggestions = new ArrayList<>();
		
		for (ContentSuggestion suggestion : suggestions) {
			
			ContentRecord contentRec = suggestion.getContent();
			String contentQId = contentRec.getContentQId();
			String templateId = ProcessContext.get().getTemplateId();
			
			String recIdentity = ContentDataUtil.getRecordIdentity(contentRec.getRecord());
			
			uploadDoc(suggestion, saveAsDraft);
			
			Map<String, Object> savedData = null;
			
			if (saveAsDraft != null && saveAsDraft) {
				
				ContentDetail contentDetails = new ContentDetail();
				contentDetails.setAddRequest(!StringUtil.hasText(recIdentity));
				contentDetails.setContentQId(contentRec.getContentQId());
				contentDetails.setData(contentRec.getRecord());
				
				@SuppressWarnings("unchecked")
				StateChangeRecording<ContentDetail> response = 
					wfManager.start(ContentApprovalConstants.WORKFLOW_NAME, 
						ContentApprovalConstants.ACTION_SAVE_DRAFT, contentDetails);
				
				savedData = response.getObjectRef().getData();
				
			} else {
				
				UpdateContentRequest update = new UpdateContentRequest(
					templateId, null, contentQId, contentRec.getRecord());
				update.setQualityAlertProcessing(QualityAlertProcessing.CONTINUE);
			
				UpdateContentResponse response = dataMgr.saveData(update);
				savedData = response.getContentRecord();
			}
			
			ContentDataRecord savedContent = new ContentDataRecord(templateId, contentQId, savedData);
			savedSuggestions.add(new ContentSuggestion(savedContent, suggestion.getOpinionBy(), suggestion.getConfidence()));
		}
		
		return savedSuggestions;
	}

	private void uploadDoc(ContentSuggestion suggestion, Boolean saveAsDraft) throws IOException {
		
		Map<String, Object> record = suggestion.getContent().getRecord();
		boolean temporaryDoc = saveAsDraft != null && saveAsDraft;
		
		for (Map.Entry<String, Object> entry : record.entrySet()) {
			
			Object attrValue = entry.getValue();
			
			if (attrValue instanceof DocAttachment) {
				
				DocAttachment docAttach = (DocAttachment) attrValue;
				
				String contentQId = suggestion.getContent().getContentQId();
				Document<DocumentMetadata> document = 
	            		docManager.buildDocument(docAttach.getInputStream(), 
	            				docAttach.getFilename(), docAttach.getContentType(), 
	            				contentQId + "." + entry.getKey(), 
	            				docAttach.getFilesize(), null, temporaryDoc);
				
				DocumentMetadata docMd = docManager.saveUsingContainerInfo(document, contentQId, null, false);
				
				Map<?, ?> docMap = BeanUtil.beanToMap(docMd);
				entry.setValue(docMap);
			}
		}
	}
	
	
}
