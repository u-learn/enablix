package com.enablix.tenant.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.mongo.template.SystemMongoTemplateHolder;
import com.enablix.tenant.TenantSetupTask;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Component
public class TenantDBSetup implements TenantSetupTask {

	@Value("${new.tenant.copy.collections:ebx_role}")
	private List<String> copyCollections;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void execute(Tenant tenant) throws Exception {
		
		MongoTemplate systemTemplate = SystemMongoTemplateHolder.getSystemMongoTemplate();
		
		for (String coll : copyCollections) {
			
			DBCollection systemCollection = systemTemplate.getCollection("ref_" + coll);
			DBCollection tenantCollection = mongoTemplate.getCollection(coll);
			
			DBCursor cursor = systemCollection.find();
			
			cursor.forEach((rec) -> {
				tenantCollection.insert(rec);
			});
		}
	}
	
	@Override
	public float executionOrder() {
		return TenantSetupTaskOrder.REF_DB_SETUP;
	}


}
