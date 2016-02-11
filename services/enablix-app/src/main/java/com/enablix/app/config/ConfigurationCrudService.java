package com.enablix.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.core.domain.config.Configuration;
import com.enablix.core.mongo.config.repo.ConfigurationRepository;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Component
public class ConfigurationCrudService extends MongoRepoCrudService<Configuration> {

	@Autowired
	private ConfigurationRepository configRepo;
	
	@Override
	public BaseMongoRepository<Configuration> getRepository() {
		return configRepo;
	}

	@Override
	public Configuration merge(Configuration t, Configuration existing) {
		existing.setConfig(t.getConfig());
		return existing;
	}
	
	@Override
	public Configuration findExisting(Configuration t) {
		return configRepo.findByKey(t.getKey());
	}

}
