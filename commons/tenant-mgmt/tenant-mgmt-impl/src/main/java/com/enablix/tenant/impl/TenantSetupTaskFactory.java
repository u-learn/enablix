package com.enablix.tenant.impl;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedOrderedBeanRegistry;
import com.enablix.tenant.TenantSetupTask;

@Component
public class TenantSetupTaskFactory extends SpringBackedOrderedBeanRegistry<TenantSetupTask> {

	public Collection<TenantSetupTask> getAllTasks() {
		return registeredInstances();
	}
	
	@Override
	protected Class<TenantSetupTask> lookupForType() {
		return TenantSetupTask.class;
	}

}
