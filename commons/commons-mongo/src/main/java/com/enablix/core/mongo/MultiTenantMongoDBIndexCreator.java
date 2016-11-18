package com.enablix.core.mongo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.MappingContextEvent;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexCreator;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver.IndexDefinitionHolder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.util.Assert;

import com.enablix.core.mongo.util.MultiTenantExecutor;
import com.enablix.core.mongo.util.MultiTenantExecutor.TenantTask;
import com.enablix.core.system.repo.TenantRepository;

public class MultiTenantMongoDBIndexCreator implements
		ApplicationListener<MappingContextEvent<MongoPersistentEntity<?>, MongoPersistentProperty>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoPersistentEntityIndexCreator.class);

	private final Map<Class<?>, Boolean> classesSeen = new ConcurrentHashMap<Class<?>, Boolean>();
	private final MongoDbFactory mongoDbFactory;
	private final MongoMappingContext mappingContext;
	private final MultiTenantMongoPersistentIndexResolver indexResolver;
	private final IndexCreatorHelper indexCreatorHelper;
	
	@Autowired
	private TenantRepository tenantRepo;
	
	/**
	 * Creates a new {@link MultiTenantMongoDBIndexCreator} for the given {@link MongoMappingContext} and
	 * {@link MongoDbFactory}.
	 * 
	 * @param mappingContext must not be {@literal null}.
	 * @param mongoDbFactory must not be {@literal null}.
	 */
	public MultiTenantMongoDBIndexCreator(MongoMappingContext mappingContext, MongoDbFactory mongoDbFactory) {
		this(mappingContext, mongoDbFactory, new MultiTenantMongoPersistentIndexResolver(mappingContext));
	}
	
	/**
	 * Creates a new {@link MultiTenantMongoDBIndexCreator} for the given {@link MongoMappingContext} and
	 * {@link MongoDbFactory}.
	 * 
	 * @param mappingContext must not be {@literal null}.
	 * @param mongoDbFactory must not be {@literal null}.
	 * @param indexResolver must not be {@literal null}.
	 */
	public MultiTenantMongoDBIndexCreator(MongoMappingContext mappingContext, MongoDbFactory mongoDbFactory,
			MultiTenantMongoPersistentIndexResolver indexResolver) {

		Assert.notNull(mongoDbFactory);
		Assert.notNull(mappingContext);
		Assert.notNull(indexResolver);

		this.mongoDbFactory = mongoDbFactory;
		this.mappingContext = mappingContext;
		this.indexResolver = indexResolver;
		this.indexCreatorHelper = new IndexCreatorHelper();

	}
	
	@PostConstruct
	public void init() {
		for (MongoPersistentEntity<?> entity : mappingContext.getPersistentEntities()) {
			checkForIndexes(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(MappingContextEvent<MongoPersistentEntity<?>, MongoPersistentProperty> event) {

		if (!event.wasEmittedBy(mappingContext)) {
			return;
		}

		PersistentEntity<?, ?> entity = event.getPersistentEntity();

		// Double check type as Spring infrastructure does not consider nested generics
		if (entity instanceof MongoPersistentEntity) {
			checkForIndexes((MongoPersistentEntity<?>) entity);
		}
	}

	private void checkForIndexes(final MongoPersistentEntity<?> entity) {

		Class<?> type = entity.getType();

		if (!classesSeen.containsKey(type)) {

			this.classesSeen.put(type, Boolean.TRUE);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Analyzing class " + type + " for index information.");
			}

			checkForAndCreateIndexes(entity);
		}
	}

	private void checkForAndCreateIndexes(MongoPersistentEntity<?> entity) {

		if (entity.findAnnotation(Document.class) != null) {
			
			for (IndexDefinitionHolder indexToCreate : indexResolver.resolveIndexFor(entity.getTypeInformation())) {
			
				final IndexDefinitionHolder index = indexToCreate;
				
				if (MongoUtil.isMultiTenantMongoDbFactory(mongoDbFactory)) {
				
					MultiTenantExecutor.executeForEachTenant(tenantRepo.findAll(), new TenantTask() {

						@Override
						public void execute() {
							indexCreatorHelper.createIndex(mongoDbFactory, index);
						}
						
					});
					
				} else {
					indexCreatorHelper.createIndex(mongoDbFactory, index);
				}
			}
		}
	}

	/**
	 * Returns whether the current index creator was registered for the given {@link MappingContext}.
	 * 
	 * @param context
	 * @return
	 */
	public boolean isIndexCreatorFor(MappingContext<?, ?> context) {
		return this.mappingContext.equals(context);
	}

	
}
