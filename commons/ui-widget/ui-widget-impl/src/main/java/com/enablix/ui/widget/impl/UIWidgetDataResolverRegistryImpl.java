package com.enablix.ui.widget.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.core.domain.ui.UIWidgetDefinition;
import com.enablix.core.domain.ui.UIWidgetDefinition.Type;
import com.enablix.ui.widget.UIWidgetDataResolver;
import com.enablix.ui.widget.UIWidgetDataResolverRegistry;

@SuppressWarnings("rawtypes")
@Component
public class UIWidgetDataResolverRegistryImpl extends SpringBackedBeanRegistry<UIWidgetDataResolver> implements UIWidgetDataResolverRegistry {

	private Map<UIWidgetDefinition.Type, UIWidgetDataResolver> registry = new HashMap<>();
	
	@Override
	public UIWidgetDataResolver getResolver(Type widgetType) {
		return registry.get(widgetType);
	}

	@Override
	protected Class<UIWidgetDataResolver> lookupForType() {
		return UIWidgetDataResolver.class;
	}

	@Override
	protected void registerBean(UIWidgetDataResolver bean) {
		registry.put(bean.resolverWidgetType(), bean);
	}

}
