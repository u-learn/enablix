package com.enablix.es.core.search;

import com.enablix.commons.util.json.JsonUtil;


public abstract class AbstractQueryFilter implements SearchQueryFilter {

	public String toString() {
		return JsonUtil.toJsonString(this);
	}

}
