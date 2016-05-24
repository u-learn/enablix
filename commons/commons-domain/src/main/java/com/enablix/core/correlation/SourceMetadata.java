package com.enablix.core.correlation;

public class SourceMetadata {

	public static enum SourceType {
		CORRELATION_RULE,
		LIKE,
		SHARE
	}
	
	private SourceType type;

	public SourceMetadata(SourceType type) {
		super();
		this.type = type;
	}

	public SourceType getType() {
		return type;
	}

	public void setType(SourceType type) {
		this.type = type;
	}
	
}
