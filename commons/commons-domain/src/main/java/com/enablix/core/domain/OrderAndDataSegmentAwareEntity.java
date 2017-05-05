package com.enablix.core.domain;

import com.enablix.core.api.OrderAware;

public abstract class OrderAndDataSegmentAwareEntity extends DataSegmentAwareEntity implements OrderAware {

	private int order;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
}