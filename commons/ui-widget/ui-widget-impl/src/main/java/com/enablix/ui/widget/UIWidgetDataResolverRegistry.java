package com.enablix.ui.widget;

import com.enablix.core.domain.ui.UIWidgetDefinition;

public interface UIWidgetDataResolverRegistry {

	@SuppressWarnings("rawtypes")
	UIWidgetDataResolver getResolver(UIWidgetDefinition.Type widgetType);
	
}
