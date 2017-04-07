package com.enablix.data.segment.view;

import java.util.HashMap;
import java.util.Map;

import com.enablix.data.view.DataView;
import com.enablix.data.view.DatastoreView;

public class DefaultDataView implements DataView {

	private Map<String, DatastoreView> datastoreViews;
	
	public DefaultDataView() {
		datastoreViews = new HashMap<>();
	}
	
	@Override
	public <T extends DatastoreView> T getDatastoreView(String storeType, Class<T> viewType) {
		
		DatastoreView dsView = datastoreViews.get(storeType);
		
		if (dsView == null) {
			throw new IllegalArgumentException("Data store view not found for store type [" + storeType + "]");
		}
		
		if (viewType.isInstance(dsView)) {
			return viewType.cast(dsView);
		}
		
		throw new IllegalArgumentException("Found data store view of type [" 
				+ dsView.getClass().getCanonicalName() + "]; expected [" + viewType.getCanonicalName() + "]");
	}

	
	public void addDatastoreView(DatastoreView dsView) {
		datastoreViews.put(dsView.datastoreType(), dsView);
	}
	
}
