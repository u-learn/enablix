package com.enablix.core.mongo.counter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContextEvent;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.OrderAware;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mongo.counter.repo.CounterDataRepository;
import com.enablix.core.mongo.util.MultiTenantExecutor;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.tenant.TenantSetupTask;

@Component
public class CounterFactoryImpl implements CounterFactory, ApplicationListener<MappingContextEvent<?, ?>>, TenantSetupTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CounterFactoryImpl.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired
	private CounterDataRepository counterRepo;
	
	private Map<String, Class<?>> counterNameToEntityClazz = new HashMap<>();
	
	@Override
	public Counter getCounter(String counterName) {
		
		return () -> {
		
			Query query = Query.query(Criteria.where("counterName").is(counterName));
			
			Update update = new Update();
			update.inc("currentValue", 1);
			
			CounterData counterData = mongoTemplate.findAndModify(query, update, 
					new FindAndModifyOptions().returnNew(true), CounterData.class);
			
			return counterData.getCurrentValue();
		};
	}

	@Override
	public void onApplicationEvent(MappingContextEvent<?, ?> event) {
		
		PersistentEntity<?, ?> entity = event.getPersistentEntity();

		// Double check type as Spring infrastructure does not consider nested generics
		if (entity instanceof MongoPersistentEntity) {
			
			MongoPersistentEntity<?> persistentEntity = (MongoPersistentEntity<?>) entity;
			Class<?> entityClazz = entity.getType();
			
			if (OrderAware.class.isAssignableFrom(entityClazz)) {
				
				String counterName = persistentEntity.getCollection();
				
				List<Tenant> tenants = tenantRepo.findAll();
				
				MultiTenantExecutor.executeForEachTenant(tenants, () -> {
					checkAndCreateCounter(counterName);
				});
				
				counterNameToEntityClazz.put(counterName, entityClazz);
			}
			
		}
		
	}
	
	private void checkAndCreateCounter(String counterName) {
	
		// check whether a counter exists for this class
		CounterData counterData = counterRepo.findByCounterName(counterName);
		
		if (counterData == null) {
			
			LOGGER.info("Creating counter [{}] for tenant [{}]", 
					counterName, ProcessContext.get().getTenantId());
			
			counterData = new CounterData();
			counterData.setCounterName(counterName);
			counterData.setCurrentValue(0);
			
			counterRepo.save(counterData);
		}
	}

	@Override
	public void execute(Tenant tenant) throws Exception {
		
		counterNameToEntityClazz.keySet().forEach((counterName) -> {
			checkAndCreateCounter(counterName);
		});
		
	}

	@Override
	public float executionOrder() {
		return TenantSetupTask.MAX_EXEC_ORDER;
	}
	
	
	
}
