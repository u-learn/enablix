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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceMetadata other = (SourceMetadata) obj;
		if (type != other.type)
			return false;
		return true;
	}
	
}
