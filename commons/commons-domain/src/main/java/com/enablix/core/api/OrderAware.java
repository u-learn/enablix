package com.enablix.core.api;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.enablix.commons.constants.AppConstants;

public interface OrderAware {

	int getOrder();
	
	void setOrder(int order);
	
	Sort SORT_BY_ORDER = new Sort(Direction.ASC, AppConstants.ORDER_ATTR_ID);
	
}
