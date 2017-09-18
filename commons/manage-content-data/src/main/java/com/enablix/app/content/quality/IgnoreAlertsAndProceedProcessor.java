package com.enablix.app.content.quality;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentPersistOperation;
import com.enablix.app.content.QualityAlertProcessor;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.core.domain.content.quality.QualityAnalysis;
import com.enablix.app.content.update.UpdateContentResponse;

@Component
public class IgnoreAlertsAndProceedProcessor implements QualityAlertProcessor {

	@Override
	public UpdateContentResponse processQualityAlerts(QualityAnalysis analysis, UpdateContentRequest request,
			ContentPersistOperation persistor) {
		
		Map<String, Object> updatedRecord = persistor.persist(request);
		
		return new UpdateContentResponse(updatedRecord, analysis);
	}

	@Override
	public QualityAlertProcessing handles() {
		return QualityAlertProcessing.CONTINUE;
	}

}
