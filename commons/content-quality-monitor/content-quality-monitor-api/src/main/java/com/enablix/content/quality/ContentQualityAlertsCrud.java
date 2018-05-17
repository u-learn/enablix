package com.enablix.content.quality;

import com.enablix.app.service.CrudService;
import com.enablix.core.domain.content.quality.ContentQualityAnalysis;

@SuppressWarnings("deprecation")
public interface ContentQualityAlertsCrud extends CrudService<ContentQualityAnalysis> {

	void deleteByContentIdentity(String contentIdentity);
	
}
