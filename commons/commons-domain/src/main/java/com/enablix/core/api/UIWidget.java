package com.enablix.core.api;

import com.enablix.core.domain.ui.UIWidgetDefinition;

public class UIWidget {

	private UIWidgetDefinition definition;
	
	private Object data;

	public UIWidgetDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(UIWidgetDefinition definition) {
		this.definition = definition;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
