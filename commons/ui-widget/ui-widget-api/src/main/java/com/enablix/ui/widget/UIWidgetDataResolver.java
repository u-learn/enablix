package com.enablix.ui.widget;

import com.enablix.core.domain.ui.UIWidgetDefinition;
import com.enablix.data.view.DataView;

public interface UIWidgetDataResolver<T extends UIWidgetDefinition> {

	Object getData(T widgetDef, DataView view, UIWidgetDataRequest dataRequest);

	UIWidgetDefinition.Type resolverWidgetType();
	
}
