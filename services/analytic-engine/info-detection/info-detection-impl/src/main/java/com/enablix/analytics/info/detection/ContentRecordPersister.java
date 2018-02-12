package com.enablix.analytics.info.detection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.beans.BeanUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.DocAttachment;

public class ContentRecordPersister {

	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private DocumentManager docManager;
	
	public List<ContentSuggestion> persist(List<ContentSuggestion> suggestions) throws IOException {
		
		List<ContentSuggestion> savedSuggestions = new ArrayList<>();
		
		for (ContentSuggestion suggestion : suggestions) {
			
			String contentQId = suggestion.getContent().getContentQId();
			String templateId = ProcessContext.get().getTemplateId();
			
			uploadDoc(suggestion);
			
			UpdateContentRequest update = new UpdateContentRequest(
					templateId, null, contentQId, suggestion.getContent().getRecord());
			update.setQualityAlertProcessing(QualityAlertProcessing.CONTINUE);
			
			UpdateContentResponse response = dataMgr.saveData(update);
			
			ContentDataRecord savedContent = new ContentDataRecord(templateId, contentQId, response.getContentRecord());
			savedSuggestions.add(new ContentSuggestion(savedContent, suggestion.getOpinionBy(), suggestion.getConfidence()));
		}
		
		return savedSuggestions;
	}

	private void uploadDoc(ContentSuggestion suggestion) throws IOException {
		
		Map<String, Object> record = suggestion.getContent().getRecord();
		
		for (Map.Entry<String, Object> entry : record.entrySet()) {
			
			Object attrValue = entry.getValue();
			
			if (attrValue instanceof DocAttachment) {
				
				DocAttachment docAttach = (DocAttachment) attrValue;
				
				String contentQId = suggestion.getContent().getContentQId();
				Document<DocumentMetadata> document = 
	            		docManager.buildDocument(docAttach.getInputStream(), 
	            				docAttach.getFilename(), docAttach.getContentType(), 
	            				contentQId + "." + entry.getKey(), 
	            				docAttach.getFilesize(), null, false);
				
				DocumentMetadata docMd = docManager.saveUsingContainerInfo(document, contentQId, null, false);
				
				Map<?, ?> docMap = BeanUtil.beanToMap(docMd);
				entry.setValue(docMap);
			}
		}
	}
	
	
}
