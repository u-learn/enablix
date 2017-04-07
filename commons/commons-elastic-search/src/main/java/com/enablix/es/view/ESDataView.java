package com.enablix.es.view;

import java.util.Collection;

import org.elasticsearch.index.query.QueryBuilder;

import com.enablix.commons.constants.AppConstants;
import com.enablix.data.view.DatastoreView;

public interface ESDataView extends DatastoreView {

	boolean isTypeVisible(String type);
	
	TypeView typeView(String type);
	
	ESDataView ALL_DATA = new ESDataView() {
		
		@Override
		public TypeView typeView(final String type) {

			return new TypeView() {
				
				@Override
				public String typeName() {
					return type;
				}
				
				@Override
				public boolean isVisible() {
					return true;
				}
				
				@Override
				public QueryBuilder baseQuery() {
//					Calendar date = Calendar.getInstance();
//					date.add(Calendar.MONTH, -9);
//					return QueryBuilders.rangeQuery("createdAt").from(date.getTime());
					return null;
				}
			};
		}
		
		@Override
		public boolean isTypeVisible(String type) {
			return true;
		}
		
		@Override
		public String datastoreType() {
			return AppConstants.ELASTICSEARCH_DATASTORE;
		}

		@Override
		public Collection<String> checkAndReturnVisibleTypes(Collection<String> allTypes) {
			return allTypes;
		}
		
	};

	Collection<String> checkAndReturnVisibleTypes(Collection<String> allTypes);
	
}
