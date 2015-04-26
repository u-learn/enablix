package com.enablix.core.mongo;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.query.MongoQueryMethod;
import org.springframework.data.mongodb.repository.query.PartTreeMongoQuery;
import org.springframework.data.mongodb.repository.query.StringBasedMongoQuery;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.mongodb.repository.support.QueryDslMongoRepository;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;

import com.enablix.core.mongo.template.SystemMongoTemplateHolder;

public class EnablixMongoRepoFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> 
		extends MongoRepositoryFactoryBean<T, S, ID> {

	@Override
	protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
		return new EnablixMongoRepositoryFactory(operations);
	}
	
	private static class EnablixMongoRepositoryFactory extends MongoRepositoryFactory {

		private MongoOperations mongoOperations;
		private MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;
		
		public EnablixMongoRepositoryFactory(MongoOperations mongoOperations) {
			super(mongoOperations);
			this.mongoOperations = mongoOperations;
			this.mappingContext = mongoOperations.getConverter().getMappingContext();
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected Object getTargetRepository(RepositoryMetadata metadata) {

			Class<?> repositoryInterface = metadata.getRepositoryInterface();
			MongoEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

			MongoOperations mongoOper = this.mongoOperations;
			mongoOper = resolveMongoOperations(entityInformation, mongoOper);
			
			if (isQueryDslRepository(repositoryInterface)) {
				return new QueryDslMongoRepository(entityInformation, mongoOper);
			} else {
				return new SimpleMongoRepository(entityInformation, mongoOper);
			}
		}
		
		@Override
		protected QueryLookupStrategy getQueryLookupStrategy(Key key) {
			return new MongoQueryLookupStrategy();
		}
		
		/**
		 * {@link QueryLookupStrategy} to create {@link PartTreeMongoQuery} instances.
		 * 
		 * @author Oliver Gierke
		 */
		private class MongoQueryLookupStrategy implements QueryLookupStrategy {

			/*
			 * (non-Javadoc)
			 * @see org.springframework.data.repository.query.QueryLookupStrategy#resolveQuery(java.lang.reflect.Method, org.springframework.data.repository.core.RepositoryMetadata, org.springframework.data.repository.core.NamedQueries)
			 */
			public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries) {

				MongoQueryMethod queryMethod = new MongoQueryMethod(method, metadata, mappingContext);
				String namedQueryName = queryMethod.getNamedQueryName();

				MongoOperations mongoOper = EnablixMongoRepositoryFactory.this.mongoOperations;
				mongoOper = resolveMongoOperations(getEntityInformation(metadata.getDomainType()), mongoOper);

				if (namedQueries.hasQuery(namedQueryName)) {
					String namedQuery = namedQueries.getQuery(namedQueryName);
					return new StringBasedMongoQuery(namedQuery, queryMethod, mongoOperations);
				} else if (queryMethod.hasAnnotatedQuery()) {
					return new StringBasedMongoQuery(queryMethod, mongoOperations);
				} else {
					return new PartTreeMongoQuery(queryMethod, mongoOperations);
				}
			}
		}
		
		private MongoOperations resolveMongoOperations(
				MongoEntityInformation<?, Serializable> metadata, MongoOperations mongoOperations) {
			
			if (metadata.getCollectionName().startsWith("ebx")) {
				mongoOperations = SystemMongoTemplateHolder.getSystemMongoTemplate();
			}
			
			return mongoOperations;
		}
		
		private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
			return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
		}
		
	}
	
}
