package com.enablix.data.view.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.segment.view.DatastoreViewBuilder;

@Component
public class MongoDataViewBuilder implements DatastoreViewBuilder<MongoDataView> {

	@Autowired
	private CollectionViewBuilder collViewBuilder;
	
	@Override
	public MongoDataView allDataView() {
		return MongoDataView.ALL_DATA;
	}

	@Override
	public MongoDataView build(DataSegment dataSegment, TemplateFacade template) {
		
		if (dataSegment == null || CollectionUtil.isEmpty(dataSegment.getAttributes())) {
			return allDataView();
		}
		
		MongoDataView mongoDataView = new DefaultMongoDataView(dataSegment, collViewBuilder);
		return mongoDataView;
	}

}
