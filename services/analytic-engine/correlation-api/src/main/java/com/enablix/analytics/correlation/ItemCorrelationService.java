package com.enablix.analytics.correlation;

import java.util.List;

import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.data.view.DataView;

public interface ItemCorrelationService {
	
	void deleteCorrelationsForItem(ContentDataRef item);

	List<ContentDataRecord> fetchCorrelatedEntityRecords(TemplateFacade template, 
			ContentDataRef item, List<String> relatedItemQIds, List<String> tags, DataView view);

	void correlateItems(ContentDataRef item, CorrelationContext context);
	
}
