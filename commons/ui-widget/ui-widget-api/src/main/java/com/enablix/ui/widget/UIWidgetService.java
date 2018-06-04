package com.enablix.ui.widget;

import com.enablix.core.api.UIWidget;
import com.enablix.data.view.DataView;

public interface UIWidgetService {

	UIWidget getWidget(String widgetIdentity, DataView dataView, UIWidgetDataRequest dataRequest);
	
}
