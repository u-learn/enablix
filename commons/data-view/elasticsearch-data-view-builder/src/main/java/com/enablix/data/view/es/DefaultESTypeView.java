package com.enablix.data.view.es;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;

import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.es.view.TypeView;

public class DefaultESTypeView implements TypeView {

	private String typeName;
	
	private boolean visible;
	
	private List<DataSegmentAttrFilter> filters;
	
	private DSAttrFilterToQueryTx queryBuilder;
	
	public DefaultESTypeView(String typeName, boolean visible, 
			List<DataSegmentAttrFilter> filters, DSAttrFilterToQueryTx queryBuilder) {
		super();
		this.typeName = typeName;
		this.visible = visible;
		this.filters = filters;
		this.queryBuilder = queryBuilder;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public String typeName() {
		return typeName;
	}

	@Override
	public QueryBuilder baseQuery() {
		return queryBuilder.buildQuery(filters, typeName);
	}

}
