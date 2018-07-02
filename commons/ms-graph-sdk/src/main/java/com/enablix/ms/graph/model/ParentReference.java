package com.enablix.ms.graph.model;

public class ParentReference {

	protected String driveId;
    protected String id;
    protected String path;

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

	@Override
	public String toString() {
		return "ParentReference [driveId=" + driveId + ", id=" + id + ", path=" + path + "]";
	}
    
}
