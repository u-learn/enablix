package com.enablix.core.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoExceptionTranslator;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver.IndexDefinitionHolder;
import org.springframework.util.ObjectUtils;

import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class IndexCreatorHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexCreatorHelper.class);
	
	public void createIndex(MongoDbFactory mongoDbFactory, IndexDefinitionHolder indexDefinition) {

		try {

			mongoDbFactory.getDb().getCollection(indexDefinition.getCollection()).createIndex(indexDefinition.getIndexKeys(),
					indexDefinition.getIndexOptions());

		} catch (MongoException ex) {

			if (MongoExceptionTranslator.MongoDbErrorCodes.isDataIntegrityViolationCode(ex.getCode())) {

				DBObject existingIndex = fetchIndexInformation(mongoDbFactory, indexDefinition);
				String message = "Cannot create index for '%s' in collection '%s' with keys '%s' and options '%s'.";

				if (existingIndex != null) {
					message += " Index already defined as '%s'.";
				}

				throw new DataIntegrityViolationException(
						String.format(message, indexDefinition.getPath(), indexDefinition.getCollection(),
								indexDefinition.getIndexKeys(), indexDefinition.getIndexOptions(), existingIndex),
						ex);
			}

			RuntimeException exceptionToThrow = mongoDbFactory.getExceptionTranslator().translateExceptionIfPossible(ex);

			throw exceptionToThrow != null ? exceptionToThrow : ex;
		}
	}
	
	private DBObject fetchIndexInformation(MongoDbFactory mongoDbFactory, IndexDefinitionHolder indexDefinition) {

		if (indexDefinition == null) {
			return null;
		}

		try {

			Object indexNameToLookUp = indexDefinition.getIndexOptions().get("name");

			for (DBObject index : mongoDbFactory.getDb().getCollection(indexDefinition.getCollection()).getIndexInfo()) {
				if (ObjectUtils.nullSafeEquals(indexNameToLookUp, index.get("name"))) {
					return index;
				}
			}

		} catch (Exception e) {
			LOGGER.debug(
					String.format("Failed to load index information for collection '%s'.", indexDefinition.getCollection()), e);
		}

		return null;
	}

	
}
