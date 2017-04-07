package com.enablix.data.view.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.es.view.ESDataView;
import com.enablix.es.view.TypeView;

public class DefaultESDataView implements ESDataView {

	private DataSegment dataSegment;
	
	private ESTypeViewBuilder typeViewBuilder;
	
	private DSAttrFilterToQueryTx queryBuilder;
	
	private Map<String, TypeView> typeViews;
	
	public DefaultESDataView(DataSegment ds, 
			ESTypeViewBuilder typeViewBuilder, DSAttrFilterToQueryTx queryBuilder) {
		
		super();
		this.dataSegment = ds;
		this.typeViewBuilder = typeViewBuilder;
		this.queryBuilder = queryBuilder;
		this.typeViews = new HashMap<>();
		
	}

	@Override
	public String datastoreType() {
		return AppConstants.ELASTICSEARCH_DATASTORE;
	}

	@Override
	public boolean isTypeVisible(String type) {
		TypeView typeView = typeView(type);
		return typeView == null ? true : typeView.isVisible();
	}

	@Override
	public TypeView typeView(String type) {
		
		TypeView typeView = typeViews.get(type);
		
		if (typeView == null) {
			
			boolean visible = typeViewBuilder.isTypeVisible(type, dataSegment);
			List<DataSegmentAttrFilter> filters = typeViewBuilder.createDataSegmentFilters(type, dataSegment);
			
			typeView = new DefaultESTypeView(type, visible, filters, queryBuilder);
			
			typeViews.put(type, typeView);
		}
		
		return typeView;
	}

	@Override
	public Collection<String> checkAndReturnVisibleTypes(Collection<String> allTypes) {
		
		Collection<String> result = new ArrayList<>();
		
		for (String type : allTypes) {
			
			TypeView typeView = typeView(type);
			
			// if  type view cannot be created, then let the type be visible
			if (typeView == null || typeView.isVisible()) {
				result.add(type);
			}
		}
		
		return result;
	}

}
