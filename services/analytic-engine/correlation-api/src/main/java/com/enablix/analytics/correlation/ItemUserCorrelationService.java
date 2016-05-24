package com.enablix.analytics.correlation;

import java.util.List;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.Tag;
import com.enablix.core.correlation.ItemUserCorrelation;

public interface ItemUserCorrelationService {

	void correlateUsers(ContentDataRef item);
	
	List<ItemUserCorrelation> findCorrelatedUsers(ContentDataRef item);
	
	List<ItemUserCorrelation> findCorrelatedUsers(ContentDataRef item, Tag tag);
	
}
