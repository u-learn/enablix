package com.enablix.analytics.correlation;

import java.util.List;

import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.services.util.template.TemplateWrapper;

public interface ItemCorrelationService {
	
	void deleteCorrelationsForItem(ContentDataRef item);

	List<ContentDataRecord> fetchCorrelatedEntityRecords(TemplateWrapper template, 
			ContentDataRef item, List<String> relatedItemQIds, List<String> tags);

	void correlateItems(ContentDataRef item, CorrelationContext context);
	
}
