package com.enablix.app.content;

import com.enablix.app.content.update.UpdateContentRequest.QualityAlertProcessing;

public interface QualityAlertProcessorFactory {

	QualityAlertProcessor getProcessor(QualityAlertProcessing alertProcessing);
	
}
