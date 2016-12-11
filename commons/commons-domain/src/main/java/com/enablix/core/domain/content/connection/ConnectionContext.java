package com.enablix.core.domain.content.connection;

import java.util.List;

public class ConnectionContext {

	private List<ConnectionContextAttribute> contextAttributes;

	public List<ConnectionContextAttribute> getContextAttributes() {
		return contextAttributes;
	}

	public void setContextAttributes(List<ConnectionContextAttribute> contextAttributes) {
		this.contextAttributes = contextAttributes;
	}
	
}
