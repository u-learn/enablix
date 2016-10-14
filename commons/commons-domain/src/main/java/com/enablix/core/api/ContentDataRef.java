package com.enablix.core.api;

public class ContentDataRef {

	private String templateId;
	
	private String containerQId;
	
	private String instanceIdentity;
	
	private String title;

	protected ContentDataRef() {
		// for JSON conversions
	}
	
	public ContentDataRef(String templateId, String containerQId, 
			String instanceIdentity, String title) {
		super();
		this.templateId = templateId;
		this.containerQId = containerQId;
		this.instanceIdentity = instanceIdentity;
		this.title = title;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public String getInstanceIdentity() {
		return instanceIdentity;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public void setInstanceIdentity(String instanceIdentity) {
		this.instanceIdentity = instanceIdentity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerQId == null) ? 0 : containerQId.hashCode());
		result = prime * result + ((instanceIdentity == null) ? 0 : instanceIdentity.hashCode());
		result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
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
		ContentDataRef other = (ContentDataRef) obj;
		if (containerQId == null) {
			if (other.containerQId != null)
				return false;
		} else if (!containerQId.equals(other.containerQId))
			return false;
		if (instanceIdentity == null) {
			if (other.instanceIdentity != null)
				return false;
		} else if (!instanceIdentity.equals(other.instanceIdentity))
			return false;
		if (templateId == null) {
			if (other.templateId != null)
				return false;
		} else if (!templateId.equals(other.templateId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ContentDataRef [templateId=" + templateId + ", containerQId=" + containerQId + ", instanceIdentity="
				+ instanceIdentity + "]";
	}
	
}
