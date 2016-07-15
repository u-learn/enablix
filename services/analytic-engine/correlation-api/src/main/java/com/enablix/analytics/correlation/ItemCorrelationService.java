package com.enablix.analytics.correlation;

import java.util.List;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.correlation.ItemItemCorrelation;

public interface ItemCorrelationService {
	
	void correlateItems(ContentDataRef item);

	List<ItemItemCorrelation> fetchItemItemCorrelations(ContentDataRef item);
	
	List<ItemItemCorrelation> fetchItemItemCorrelations(ContentDataRef item, List<String> relatedItemQIds);
	
	void deleteCorrelationsForItem(ContentDataRef item);
	
}
