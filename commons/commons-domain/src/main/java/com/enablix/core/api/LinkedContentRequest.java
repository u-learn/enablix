package com.enablix.core.api;

public class LinkedContentRequest {
	
	private String refContentQId;
	private String refMatchAttrId;
	private String refMatchAttrValue;
	
	private String mapContentQId;
	private String mapMatchAttrId;
	private String mapMatchAttrValue;
	
	private String lookupContentQId;
	
	private String contentBusinessCategory;

	public String getRefContentQId() {
		return refContentQId;
	}

	public void setRefContentQId(String refContentQId) {
		this.refContentQId = refContentQId;
	}

	public String getRefMatchAttrId() {
		return refMatchAttrId;
	}

	public void setRefMatchAttrId(String refMatchAttrId) {
		this.refMatchAttrId = refMatchAttrId;
	}

	public String getRefMatchAttrValue() {
		return refMatchAttrValue;
	}

	public void setRefMatchAttrValue(String refMatchAttrValue) {
		this.refMatchAttrValue = refMatchAttrValue;
	}

	public String getMapContentQId() {
		return mapContentQId;
	}

	public void setMapContentQId(String mapContentQId) {
		this.mapContentQId = mapContentQId;
	}

	public String getMapMatchAttrId() {
		return mapMatchAttrId;
	}

	public void setMapMatchAttrId(String mapMatchAttrId) {
		this.mapMatchAttrId = mapMatchAttrId;
	}

	public String getMapMatchAttrValue() {
		return mapMatchAttrValue;
	}

	public void setMapMatchAttrValue(String mapMatchAttrValue) {
		this.mapMatchAttrValue = mapMatchAttrValue;
	}

	public String getLookupContentQId() {
		return lookupContentQId;
	}

	public void setLookupContentQId(String lookupContentQId) {
		this.lookupContentQId = lookupContentQId;
	}

	public String getContentBusinessCategory() {
		return contentBusinessCategory;
	}

	public void setContentBusinessCategory(String contentBusinessCategory) {
		this.contentBusinessCategory = contentBusinessCategory;
	}
	
}