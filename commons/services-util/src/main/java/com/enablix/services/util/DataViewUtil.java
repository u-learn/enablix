package com.enablix.services.util;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.segment.view.DataViewBuilder;
import com.enablix.data.view.DataView;
import com.enablix.es.view.ESDataView;

public class DataViewUtil {
	
	private static DataViewBuilder dataViewBuilder;
	
	private static DataView ALL_DATA_VIEW;
	
	public static void registerDataViewBuilder(DataViewBuilder dataViewBuilder) {
		DataViewUtil.dataViewBuilder = dataViewBuilder;
	}
	
	public static DataView allDataView() {
		
		if (ALL_DATA_VIEW == null) {
			synchronized (DataViewUtil.class) {
				ALL_DATA_VIEW = dataViewBuilder.allDataView();
			}
		}
		
		return ALL_DATA_VIEW;
	}
	
	public static MongoDataView getMongoDataView(DataView view) {
		return view.getDatastoreView(AppConstants.MONGO_DATASTORE, MongoDataView.class);
	}
	
	public static ESDataView getElasticSearchDataView(DataView view) {
		return view.getDatastoreView(AppConstants.ELASTICSEARCH_DATASTORE, ESDataView.class);
	}
	
}
