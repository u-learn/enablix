package com.enablix.analytics.info.detection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;

public class ContentRecordPersister {

	@Autowired
	private ContentDataManager dataMgr;
	
	public List<ContentSuggestion> persist(List<ContentSuggestion> suggestions) {
		
		List<ContentSuggestion> savedSuggestions = new ArrayList<>();
		
		for (ContentSuggestion suggestion : suggestions) {
			
			String contentQId = suggestion.getContent().getContentQId();
			String templateId = ProcessContext.get().getTemplateId();
			
			UpdateContentRequest update = new UpdateContentRequest(
					templateId, null, contentQId, suggestion.getContent().getRecord());
			update.setQualityAlertProcessing(QualityAlertProcessing.CONTINUE);
			
			UpdateContentResponse response = dataMgr.saveData(update);
			
			ContentDataRecord savedContent = new ContentDataRecord(templateId, contentQId, response.getContentRecord());
			savedSuggestions.add(new ContentSuggestion(savedContent, suggestion.getOpinionBy(), suggestion.getConfidence()));
		}
		
		return savedSuggestions;
	}
	
	
}
