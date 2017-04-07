package com.enablix.data.view.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.segment.view.DatastoreViewBuilder;
import com.enablix.es.view.ESDataView;

@Component
public class ElasticsearchDataViewBuilder implements DatastoreViewBuilder<ESDataView> {

	@Autowired
	private ESTypeViewBuilder typeViewBuilder;
	
	@Autowired
	private DSAttrFilterToQueryTx queryBuilder;
	
	@Override
	public ESDataView build(DataSegment dataSegment, TemplateFacade template) {
		
		if (dataSegment == null || CollectionUtil.isEmpty(dataSegment.getAttributes())) {
			return allDataView();
		}
		
		DefaultESDataView esDataView = new DefaultESDataView(dataSegment, typeViewBuilder, queryBuilder);
		return esDataView;
	}

	@Override
	public ESDataView allDataView() {
		return ESDataView.ALL_DATA;
	}

}
