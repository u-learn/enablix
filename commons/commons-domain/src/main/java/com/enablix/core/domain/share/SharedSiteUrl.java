package com.enablix.core.domain.share;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_shared_site_url")
public class SharedSiteUrl extends BaseDocumentEntity {

	private String sharedWith;
	
	private String sharedUrl;
	
	private String actualUrl;
	
	private String tenantId;
	
	private String templateId;
	
	public SharedSiteUrl(String sharedWith, String sharedUrl, 
			String actualUrl, String tenantId, String templateId) {
		super();
		this.sharedWith = sharedWith;
		this.sharedUrl = sharedUrl;
		this.actualUrl = actualUrl;
		this.tenantId = tenantId;
		this.templateId = templateId;
	}

	public String getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(String sharedWith) {
		this.sharedWith = sharedWith;
	}

	public String getSharedUrl() {
		return sharedUrl;
	}

	public void setSharedUrl(String sharedUrl) {
		this.sharedUrl = sharedUrl;
	}

	public String getActualUrl() {
		return actualUrl;
	}

	public void setActualUrl(String actualUrl) {
		this.actualUrl = actualUrl;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
}
