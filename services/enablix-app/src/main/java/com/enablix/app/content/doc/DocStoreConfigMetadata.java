package com.enablix.app.content.doc;

import java.util.List;

public class DocStoreConfigMetadata {

	private String storeTypeName;
	
	private String storeTypeCode;
	
	private List<DocStoreConfigParam> params;
	
	public DocStoreConfigMetadata() {
		// required for JSON conversion
	}
	
	public DocStoreConfigMetadata(String storeTypeName, String storeTypeCode) {
		super();
		this.storeTypeName = storeTypeName;
		this.storeTypeCode = storeTypeCode;
	}

	public String getStoreTypeName() {
		return storeTypeName;
	}

	public void setStoreTypeName(String storeTypeName) {
		this.storeTypeName = storeTypeName;
	}

	public String getStoreTypeCode() {
		return storeTypeCode;
	}

	public void setStoreTypeCode(String storeTypeCode) {
		this.storeTypeCode = storeTypeCode;
	}

	public List<DocStoreConfigParam> getParams() {
		return params;
	}

	public void setParams(List<DocStoreConfigParam> params) {
		this.params = params;
	}


	public static class DocStoreConfigParam {
		
		private String paramKey;
		
		private String paramName;
		
		public DocStoreConfigParam() {
			// required for JSON conversion
		}
		
		public DocStoreConfigParam(String paramKey, String paramName) {
			super();
			this.paramKey = paramKey;
			this.paramName = paramName;
		}

		public String getParamKey() {
			return paramKey;
		}

		public void setParamKey(String paramKey) {
			this.paramKey = paramKey;
		}

		public String getParamName() {
			return paramName;
		}

		public void setParamName(String paramName) {
			this.paramName = paramName;
		}

	}
	
}
