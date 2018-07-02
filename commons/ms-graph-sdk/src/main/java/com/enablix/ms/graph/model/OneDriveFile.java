package com.enablix.ms.graph.model;

public class OneDriveFile extends OneDriveItem {

	private FileProperty file;
	
	public FileProperty getFile() {
		return file;
	}

	public void setFile(FileProperty file) {
		this.file = file;
	}

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	@Override
	public String toString() {
		return "OneDriveFile [file=" + file + ", id=" + id + ", name=" + name + ", createdBy=" + createdBy
				+ ", createdDateTime=" + createdDateTime + ", lastModifiedBy=" + lastModifiedBy
				+ ", lastModifiedDateTime=" + lastModifiedDateTime + ", cTag=" + cTag + ", eTag=" + eTag + ", size="
				+ size + ", webUrl=" + webUrl + ", parentReference=" + parentReference + "]";
	}
	

}
