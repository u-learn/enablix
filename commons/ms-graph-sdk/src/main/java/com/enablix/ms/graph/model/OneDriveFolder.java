package com.enablix.ms.graph.model;

public class OneDriveFolder extends OneDriveItem {

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public boolean isFolder() {
		return true;
	}

	@Override
	public String toString() {
		return "OneDriveFolder [id=" + id + ", name=" + name + ", createdBy=" + createdBy + ", createdDateTime="
				+ createdDateTime + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDateTime="
				+ lastModifiedDateTime + ", cTag=" + cTag + ", eTag=" + eTag + ", size=" + size + ", webUrl=" + webUrl
				+ ", parentReference=" + parentReference + "]";
	}

}
