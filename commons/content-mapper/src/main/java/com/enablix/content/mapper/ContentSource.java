package com.enablix.content.mapper;

public final class ContentSource {

	private final String sourceId;
	
	private final String tenantId;

	public ContentSource(String sourceId, String tenantId) {
		super();
		this.sourceId = sourceId;
		this.tenantId = tenantId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public String getTenantId() {
		return tenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());
		result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
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
		ContentSource other = (ContentSource) obj;
		if (sourceId == null) {
			if (other.sourceId != null)
				return false;
		} else if (!sourceId.equals(other.sourceId))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ContentSource [sourceId=" + sourceId + ", tenantId=" + tenantId + "]";
	}
	
}
