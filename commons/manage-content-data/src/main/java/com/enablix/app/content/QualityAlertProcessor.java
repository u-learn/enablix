package com.enablix.app.content;

import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.core.domain.content.quality.QualityAnalysis;
import com.enablix.app.content.update.UpdateContentResponse;

public interface QualityAlertProcessor {

	UpdateContentResponse processQualityAlerts(QualityAnalysis analysis, UpdateContentRequest request, ContentPersistOperation persistor);
	
	QualityAlertProcessing handles();
	
}
