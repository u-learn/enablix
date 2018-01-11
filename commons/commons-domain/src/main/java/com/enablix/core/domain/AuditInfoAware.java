package com.enablix.core.domain;

import java.util.Date;

public interface AuditInfoAware {

	public Date getCreatedAt();

	public Date getModifiedAt();

	public String getCreatedBy();

	public String getModifiedBy();

	public String getCreatedByName();

	public String getModifiedByName();

}
