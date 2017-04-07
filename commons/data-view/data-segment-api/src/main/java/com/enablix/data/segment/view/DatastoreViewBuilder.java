package com.enablix.data.segment.view;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DatastoreView;

public interface DatastoreViewBuilder<T extends DatastoreView> {

	T build(DataSegment dataSegment, TemplateFacade template);
	
	T allDataView();
	
}
