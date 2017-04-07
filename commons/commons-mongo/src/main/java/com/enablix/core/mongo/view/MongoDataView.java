package com.enablix.core.mongo.view;

import org.springframework.data.mongodb.core.query.Criteria;

import com.enablix.commons.constants.AppConstants;
import com.enablix.data.view.DatastoreView;

public interface MongoDataView extends DatastoreView {

	CollectionView<?> getCollectionView(String collectionName);

	<T> boolean isRecordVisible(String collectionName, T record);
	
	public static MongoDataView ALL_DATA = new MongoDataView() {

		@Override
		public CollectionView<?> getCollectionView(final String collectionName) {
			
			return new CollectionView<Object>() {

				@Override
				public boolean isVisible() {
					return true;
				}

				@Override
				public Criteria viewFilter() {
//					Calendar date = Calendar.getInstance();
//					date.add(Calendar.MONTH, -9);
//					SearchFilter filter = new StringFilter("createdBy", "ebxmaster@enablix.com", ConditionOperator.EQ);
//					filter = filter.or(new StringFilter("createdBy", "system", ConditionOperator.EQ));
//					filter = filter.and(new DateFilter("createdAt", date.getTime(), ConditionOperator.GTE));
//					return filter.toPredicate(new Criteria());
					return null;
				}

				@Override
				public String collectionName() {
					return collectionName;
				}

				@Override
				public boolean isRecordVisible(Object record) {
					return true;
				}
				
			};
		}

		@Override
		public boolean isRecordVisible(String collectionName, Object record) {
			return true;
		}

		@Override
		public String datastoreType() {
			return AppConstants.MONGO_DATASTORE;
		}
		
	};

}
