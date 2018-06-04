package com.enablix.ui.widget.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.pack.ContentPackManager;
import com.enablix.core.domain.ui.ContentPackUIWidgetDef;
import com.enablix.core.domain.ui.UIWidgetDefinition;
import com.enablix.core.domain.ui.UIWidgetDefinition.Type;
import com.enablix.data.view.DataView;
import com.enablix.ui.widget.UIWidgetDataRequest;
import com.enablix.ui.widget.UIWidgetDataResolver;

@Component
public class ContentPackWidgetDataResolver implements UIWidgetDataResolver<ContentPackUIWidgetDef> {

	@Autowired
	private ContentPackManager contentPackMgr;
	
	@Override
	public Object getData(ContentPackUIWidgetDef widgetDef, DataView view, UIWidgetDataRequest dataRequest) {
		return contentPackMgr.getContentPack(widgetDef.getContentPackIdentity(), 
				view, dataRequest.getPageNo(), dataRequest.getPageSize());
	}

	@Override
	public Type resolverWidgetType() {
		return UIWidgetDefinition.Type.CONTENT_PACK;
	}

}
