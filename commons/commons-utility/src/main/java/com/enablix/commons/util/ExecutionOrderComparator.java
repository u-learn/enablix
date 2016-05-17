package com.enablix.commons.util;

import java.util.Comparator;

public class ExecutionOrderComparator implements Comparator<ExecutionOrderAware> {

	@Override
	public int compare(ExecutionOrderAware o1, ExecutionOrderAware o2) {
		final float compare = o1.executionOrder() - o2.executionOrder();
		return compare == 0 ? 0 : compare < 0 ? -1 : 1;
	}

}
