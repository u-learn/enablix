package com.enablix.data.view;

public interface DataView {

	<T extends DatastoreView> T getDatastoreView(String storeType, Class<T> viewType); 
	
}
