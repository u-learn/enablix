package com.enablix.ui.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.UIWidget;
import com.enablix.core.domain.ui.UIWidgetDefinition;
import com.enablix.data.view.DataView;
import com.enablix.ui.widget.UIWidgetService;
import com.enablix.ui.widget.repo.UIWidgetDefinitionRepository;

@Component
public class UIWidgetServiceImpl implements UIWidgetService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UIWidgetServiceImpl.class);
	
	@Autowired
	private UIWidgetDefinitionRepository repo;
	
	@Autowired
	private UIWidgetDataResolverRegistry registry;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public UIWidget getWidget(String widgetIdentity, DataView dataView, UIWidgetDataRequest dataRequest) {
		
		UIWidget widget = null;
		UIWidgetDefinition widgetDef = repo.findByIdentity(widgetIdentity);
		
		if (widgetDef != null) {
			
			UIWidgetDataResolver resolver = registry.getResolver(widgetDef.getType());
			
			if (resolver == null) {
				String errMessage = "No data resolver found for widget type [" + widgetDef.getType() + "]";
				LOGGER.error(errMessage);
				throw new IllegalStateException(errMessage);
			}
		
			if (dataRequest.getPageSize() == -1) {
				dataRequest.setPageSize(getPageSizeFromWidgetDef(widgetDef));
			}
			
			Object data = resolver.getData(widgetDef, dataView, dataRequest);
			
			widget = new UIWidget();
			widget.setData(data);
			widget.setDefinition(widgetDef);
		}
		
		return widget;
	}

	private int getPageSizeFromWidgetDef(UIWidgetDefinition widgetDef) {
		int pageSize = 0;
		if (CollectionUtil.isNotEmpty(widgetDef.getProperties())) {
			Object itemLimit = widgetDef.getProperties().get("widgetItemLimit");
			if (itemLimit instanceof Number) {
				pageSize = ((Number) itemLimit).intValue();
			}
		}
		return pageSize > 0 ? pageSize : 5;
	}


}
