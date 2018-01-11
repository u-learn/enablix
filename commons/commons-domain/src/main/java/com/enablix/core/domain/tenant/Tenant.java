package com.enablix.core.domain.tenant;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.WebPortal;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebxTenant")
public class Tenant extends BaseDocumentEntity {

	private String tenantId;
	
	private String name;
	
	private String defaultTemplateId;
	
	private String portal = WebPortal.V1.getName();
	
	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultTemplateId() {
		return defaultTemplateId;
	}

	public void setDefaultTemplateId(String defaultTemplateId) {
		this.defaultTemplateId = defaultTemplateId;
	}
	
}
