package com.enablix.app.content.quality;

import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentPersistOperation;
import com.enablix.app.content.QualityAlertProcessor;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;
import com.enablix.core.domain.content.quality.QualityAnalysis;
import com.enablix.app.content.update.UpdateContentResponse;

@Component
public class StopAndReturnAlertsProcessor implements QualityAlertProcessor {

	@Override
	public UpdateContentResponse processQualityAlerts(QualityAnalysis analysis, UpdateContentRequest request,
			ContentPersistOperation persistor) {
		return new UpdateContentResponse(request.getDataAsMap(), analysis);
	}

	@Override
	public QualityAlertProcessing handles() {
		return QualityAlertProcessing.BREAK;
	}

}
