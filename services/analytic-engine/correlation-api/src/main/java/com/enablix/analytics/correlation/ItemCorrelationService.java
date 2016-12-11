package com.enablix.analytics.correlation;

import java.util.List;

import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ItemCorrelationService {
	
	void deleteCorrelationsForItem(ContentDataRef item);

	List<ContentDataRecord> fetchCorrelatedEntityRecords(ContentTemplate template, ContentDataRef item,
			List<String> relatedItemQIds, List<String> tags);

	void correlateItems(ContentDataRef item, CorrelationContext context);
	
}
