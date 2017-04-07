package com.enablix.data.segment.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@Component
public class DataViewBuilderImpl implements DataViewBuilder {

	@Autowired
	private DatastoreViewBuilderRegistry builderRegistry;

	public DataViewBuilderImpl() {
		DataViewUtil.registerDataViewBuilder(this);
	}
	
	@Override
	public DataView createDataView(DataSegment dataSegment, TemplateFacade template) {
		
		DefaultDataView dataView = new DefaultDataView();
		
		for (DatastoreViewBuilder<?> builder : builderRegistry.getBuilders()) {
			dataView.addDatastoreView(builder.build(dataSegment, template));
		}
		
		return dataView;
	}

	@Override
	public DataView allDataView() {
		
		DefaultDataView dataView = new DefaultDataView();
		
		for (DatastoreViewBuilder<?> builder : builderRegistry.getBuilders()) {
			dataView.addDatastoreView(builder.allDataView());
		}
		
		return dataView;
	}

}
