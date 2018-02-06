package com.enablix.bayes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.enablix.core.domain.tenant.Tenant;

public class ExecutionContext extends InitializationContext {

	private Collection<Tenant> executeForTenants;
	
	private Collection<String> executeForUsers;
	
	private Map<String, Object> sharedAttributes;
	
	public ExecutionContext() {
		this.sharedAttributes = new HashMap<>();
	}
	
	public Map<String, Object> getSharedAttributes() {
		return sharedAttributes;
	}

	public void setSharedAttributes(Map<String, Object> sharedAttributes) {
		this.sharedAttributes = sharedAttributes;
	}

	public Collection<Tenant> getExecuteForTenants() {
		return executeForTenants;
	}

	public void setExecuteForTenants(Collection<Tenant> executeForTenants) {
		this.executeForTenants = executeForTenants;
	}

	public Collection<String> getExecuteForUsers() {
		return executeForUsers;
	}

	public void setExecuteForUsers(Collection<String> executeForUsers) {
		this.executeForUsers = executeForUsers;
	}

}
