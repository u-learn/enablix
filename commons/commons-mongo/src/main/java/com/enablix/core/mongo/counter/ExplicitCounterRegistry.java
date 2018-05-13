package com.enablix.core.mongo.counter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mongo.util.MultiTenantExecutor;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.tenant.TenantSetupTask;

@Component
public class ExplicitCounterRegistry implements TenantSetupTask {

	@Autowired
	private CounterFactory counterFactory;
	
	@Autowired
	private TenantRepository tenantRepo;
	
	private Set<String> explicitCounters = new HashSet<>();
	
	public void register(String counterName) {
		explicitCounters.add(counterName);
		
		List<Tenant> tenants = tenantRepo.findAll();
		
		MultiTenantExecutor.executeForEachTenant(tenants, () -> {
			counterFactory.checkAndCreateCounter(counterName);
		});
	}
	
	public Collection<String> counterNames() {
		return explicitCounters;
	}
	
	@Override
	public void execute(Tenant tenant) throws Exception {
		explicitCounters.forEach((counter) -> counterFactory.checkAndCreateCounter(counter));
	}

	@Override
	public float executionOrder() {
		return TenantSetupTask.MAX_EXEC_ORDER + 100;
	}
}
