package com.enablix.core.mongo.search;

import com.enablix.commons.util.json.JsonUtil;


public abstract class AbstractFilter implements SearchFilter {

	@Override
	public SearchFilter and(SearchFilter andWithFilter) {
		if (andWithFilter != null) {
			return new And(this, andWithFilter);
		}
		return this;
	}

	@Override
	public SearchFilter or(SearchFilter orWithFilter) {
		if (orWithFilter != null) {
			return new Or(this, orWithFilter);
		}
		return this;
	}
	
	public String toString() {
		return JsonUtil.toJsonString(this);
	}

}
