package com.enablix.ms.graph.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public abstract class OneDriveItem {

	/**
     * The OneDrive id of the resource.
     */
    protected String id = "";

    /**
     * The Name.
     */
    protected String name = "";

    /**
     * The created by reference. Possible keys are 'user', 'application' and 'device'.
     */
    protected HashMap<String, DriveUser> createdBy = new HashMap<>();

    /**
     * The creation timestamp of this item.
     */
    protected String createdDateTime;

    /**
     * The modified by reference. Possible keys are 'user', 'application' and 'device'.
     */
    protected HashMap<String, DriveUser> lastModifiedBy = new HashMap<>();

    /**
     * The last modified timestamp of this item.
     */
    protected String lastModifiedDateTime = "";

    /**
     * The cTag.
     */
    protected String cTag = "";

    /**
     * The eTag.
     */
    protected String eTag = "";

    /**
     * The size of an item in bytes.
     */
    protected long size = 0;

    /**
     * URL that displays the resource in the browser.
     */
    protected String webUrl = "";

    /**
     * The parent folder reference.
     */
    protected ParentReference parentReference;

    /**
     * The raw JSON which is received from the OneDrive API.
     */
    protected String rawJson = "";
    
    /**
     * Gets the name of the item.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the item.
     *
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets the cTag.
     *
     * @return cTag
     */
    public String getCTag() {
        return this.cTag;
    }

    /**
     * Gets the eTag.
     *
     * @return eTag
     */
    public String getETag() {
        return this.eTag;
    }

    /**
     * The created by reference. Possible keys are 'user', 'application' and 'device'.
     *
     * @return created by
     */
    public HashMap<String, DriveUser> getCreatedBy() {
        return this.createdBy;
    }

    /**
     * The creation timestamp of this item.
     *
     * @return unix formatted timestamp
     */
    public long getCreatedDateTime() {
        try {
            return this.parseTimestamp(this.createdDateTime).getTime() / 1000;
        } catch (java.text.ParseException e) {
            return 0;
        }
    }

    /**
     * The modified by reference. Possible keys are 'user', 'application' and 'device'.
     *
     * @return last modified by
     */
    public HashMap<String, DriveUser> getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    /**
     * The last modified timestamp of this item.
     *
     * @return unix formatted timestamp
     */
    public long getLastModifiedDateTime() {
        try {
            return this.parseTimestamp(this.lastModifiedDateTime).getTime() / 1000;
        } catch (java.text.ParseException e) {
            return 0;
        }
    }

    /**
     * Parse a timestamp.
     *
     * @param dateTime Format: 0000-00-00T00:00:00
     * @return timestamp
     * @throws java.text.ParseException if the date can not be parsed
     */
    private Date parseTimestamp(String dateTime) throws java.text.ParseException {
        if (dateTime != null && dateTime.indexOf('.') != -1) {
            dateTime = dateTime.substring(0, dateTime.indexOf('.'));
            DateFormat df = new SimpleDateFormat("y-M-d'T'H:m:s");
            return df.parse(dateTime);
        } else {
            throw new java.text.ParseException(dateTime, -1);
        }
    }

    /**
     * Gets the size of this item in bytes.
     *
     * @return size
     */
    public long getSize() {
        return this.size;
    }

    /**
     * Gets the URL that displays the resource in the browser.
     *
     * @return web url
     */
    public String getWebUrl() {
        return this.webUrl;
    }

    public String getcTag() {
		return cTag;
	}

	public void setcTag(String cTag) {
		this.cTag = cTag;
	}

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	public ParentReference getParentReference() {
		return parentReference;
	}

	public void setParentReference(ParentReference parentReference) {
		this.parentReference = parentReference;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreatedBy(HashMap<String, DriveUser> createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public void setLastModifiedBy(HashMap<String, DriveUser> lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setLastModifiedDateTime(String lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	/**
     * Gets the raw JSON which is received from the OneDrive API.
     *
     * @return raw json
     */
    public String getRawJson() {
        return rawJson;
    }

    /**
     * Sets the raw json.
     *
     * @param rawJson json
     * @return raw json
     */
    public OneDriveItem setRawJson(String rawJson) {
        this.rawJson = rawJson;
        return this;
    }

    /**
     * Is file.
     *
     * @return boolean
     */
    public abstract boolean isFile();

    /**
     * Is folder.
     *
     * @return boolean
     */
    public abstract boolean isFolder();

	@Override
	public String toString() {
		return "OneDriveItem [id=" + id + ", name=" + name + ", createdBy=" + createdBy + ", createdDateTime="
				+ createdDateTime + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDateTime="
				+ lastModifiedDateTime + ", cTag=" + cTag + ", eTag=" + eTag + ", size=" + size + ", webUrl=" + webUrl
				+ ", parentReference=" + parentReference + ", rawJson=" + rawJson + "]";
	}
    
    
}
