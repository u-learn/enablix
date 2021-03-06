package com.enablix.core.domain.security.authorization;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.enablix.core.domain.segment.DataSegment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSystemProfile {
	
	@DBRef
	private List<Role> roles = new ArrayList<Role>();
	
	private boolean sendWeeklyDigest;
	
	private String defaultClientId;
	
	@DBRef
	private DataSegment dataSegment;
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public boolean isSendWeeklyDigest() {
		return sendWeeklyDigest;
	}
	
	public void setSendWeeklyDigest(boolean sendWeeklyDigest) {
		this.sendWeeklyDigest = sendWeeklyDigest;
	}

	public DataSegment getDataSegment() {
		return dataSegment;
	}

	public void setDataSegment(DataSegment accessibleDataSegment) {
		this.dataSegment = accessibleDataSegment;
	}

	public String getDefaultClientId() {
		return defaultClientId;
	}

	public void setDefaultClientId(String defaultClientId) {
		this.defaultClientId = defaultClientId;
	}
	
}