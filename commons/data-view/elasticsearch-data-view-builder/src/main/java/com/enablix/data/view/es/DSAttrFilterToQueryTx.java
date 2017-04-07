package com.enablix.data.view.es;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;

import com.enablix.data.view.DataSegmentAttrFilter;

public interface DSAttrFilterToQueryTx {

	QueryBuilder buildQuery(List<DataSegmentAttrFilter> filters, String type);
	
}
