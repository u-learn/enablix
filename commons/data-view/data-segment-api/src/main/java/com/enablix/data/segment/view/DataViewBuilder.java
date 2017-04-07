package com.enablix.data.segment.view;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataView;

public interface DataViewBuilder {

	DataView createDataView(DataSegment dataSegment, TemplateFacade template);
	
	DataView allDataView();
	
}
