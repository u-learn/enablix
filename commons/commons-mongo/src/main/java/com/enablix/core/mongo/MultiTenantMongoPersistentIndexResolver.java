package com.enablix.core.mongo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.Index.Duplicates;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.enablix.mongo.index.MultiTenantIndexed;

/**
 * Copied from {@link MongoPersistentEntityIndexResolver}. Use {@link MultiTenantIndexed} instead of {@link Indexed}
 * since Indexed does not work with multi-tenant database factory
 *  
 * @author dikshit.luthra
 *
 */
public class MultiTenantMongoPersistentIndexResolver extends MongoPersistentEntityIndexResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenantMongoPersistentIndexResolver.class);
	
	private final MongoMappingContext mappingContext;
	
	public MultiTenantMongoPersistentIndexResolver(MongoMappingContext mappingContext) {
		super(mappingContext);
		this.mappingContext = mappingContext;
	}

	public List<IndexDefinitionHolder> resolveIndexForEntity(final MongoPersistentEntity<?> root) {

		Assert.notNull(root, "Index cannot be resolved for given 'null' entity.");
		Document document = root.findAnnotation(Document.class);
		Assert.notNull(document, "Given entity is not collection root.");

		final List<IndexDefinitionHolder> indexInformation = new ArrayList<MongoPersistentEntityIndexResolver.IndexDefinitionHolder>();

		final CycleGuard guard = new CycleGuard();

		root.doWithProperties(new PropertyHandler<MongoPersistentProperty>() {

			@Override
			public void doWithPersistentProperty(MongoPersistentProperty persistentProperty) {

				try {
					if (persistentProperty.isEntity()) {
						indexInformation.addAll(resolveIndexForClass(persistentProperty.getTypeInformation().getActualType(),
								persistentProperty.getFieldName(), root.getCollection(), guard));
					}

					IndexDefinitionHolder indexDefinitionHolder = createIndexDefinitionHolderForProperty(
							persistentProperty.getFieldName(), root.getCollection(), persistentProperty);
					if (indexDefinitionHolder != null) {
						indexInformation.add(indexDefinitionHolder);
					}
				} catch (CyclicPropertyReferenceException e) {
					LOGGER.info(e.getMessage());
				}
			}
		});

		return indexInformation;
	}
	
	/**
	 * Recursively resolve and inspect properties of given {@literal type} for indexes to be created.
	 * 
	 * @param type
	 * @param path The {@literal "dot} path.
	 * @param collection
	 * @return List of {@link IndexDefinitionHolder} representing indexes for given type and its referenced property
	 *         types. Will never be {@code null}.
	 */
	private List<IndexDefinitionHolder> resolveIndexForClass(final TypeInformation<?> type, final String path,
			final String collection, final CycleGuard guard) {

		MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(type);

		final List<IndexDefinitionHolder> indexInformation = new ArrayList<MongoPersistentEntityIndexResolver.IndexDefinitionHolder>();

		entity.doWithProperties(new PropertyHandler<MongoPersistentProperty>() {

			@Override
			public void doWithPersistentProperty(MongoPersistentProperty persistentProperty) {

				String propertyDotPath = (StringUtils.hasText(path) ? path + "." : "") + persistentProperty.getFieldName();
				guard.protect(persistentProperty, path);

				if (persistentProperty.isEntity()) {
					try {
						indexInformation.addAll(resolveIndexForClass(persistentProperty.getTypeInformation().getActualType(),
								propertyDotPath, collection, guard));
					} catch (CyclicPropertyReferenceException e) {
						LOGGER.info(e.getMessage());
					}
				}

				IndexDefinitionHolder indexDefinitionHolder = createIndexDefinitionHolderForProperty(propertyDotPath,
						collection, persistentProperty);
				if (indexDefinitionHolder != null) {
					indexInformation.add(indexDefinitionHolder);
				}
			}
		});

		return indexInformation;
	}

	private IndexDefinitionHolder createIndexDefinitionHolderForProperty(String dotPath, String collection,
			MongoPersistentProperty persistentProperty) {

		if (persistentProperty.isAnnotationPresent(MultiTenantIndexed.class)) {
			return createIndexDefinition(dotPath, collection, persistentProperty);
		}

		return null;
	}

	/**
	 * Creates {@link IndexDefinition} wrapped in {@link IndexDefinitionHolder} out of {@link Indexed} for given
	 * {@link MongoPersistentProperty}.
	 * 
	 * @param dotPath The properties {@literal "dot"} path representation from its document root.
	 * @param collection
	 * @param persitentProperty
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected IndexDefinitionHolder createIndexDefinition(String dotPath, String fallbackCollection,
			MongoPersistentProperty persitentProperty) {

		MultiTenantIndexed index = persitentProperty.findAnnotation(MultiTenantIndexed.class);
		String collection = fallbackCollection;

		Index indexDefinition = new Index().on(dotPath,
				IndexDirection.ASCENDING.equals(index.direction()) ? Sort.Direction.ASC : Sort.Direction.DESC);

		if (!index.useGeneratedName()) {
			indexDefinition.named(pathAwareIndexName(index.name(), dotPath, persitentProperty));
		}

		if (index.unique()) {
			indexDefinition.unique(index.dropDups() ? Duplicates.DROP : Duplicates.RETAIN);
		}

		if (index.sparse()) {
			indexDefinition.sparse();
		}

		if (index.background()) {
			indexDefinition.background();
		}

		if (index.expireAfterSeconds() >= 0) {
			indexDefinition.expire(index.expireAfterSeconds(), TimeUnit.SECONDS);
		}

		return new IndexDefinitionHolder(dotPath, indexDefinition, collection);
	}
	
	private String pathAwareIndexName(String indexName, String dotPath, MongoPersistentProperty property) {

		String nameToUse = StringUtils.hasText(indexName) ? indexName : "";

		if (!StringUtils.hasText(dotPath) || (property != null && dotPath.equals(property.getFieldName()))) {
			return StringUtils.hasText(nameToUse) ? nameToUse : dotPath;
		}

		if (StringUtils.hasText(dotPath)) {

			nameToUse = StringUtils.hasText(nameToUse)
					? (property != null ? dotPath.replace("." + property.getFieldName(), "") : dotPath) + "." + nameToUse
					: dotPath;
		}
		return nameToUse;

	}
	
	/**
	 * {@link CycleGuard} holds information about properties and the paths for accessing those. This information is used
	 * to detect potential cycles within the references.
	 * 
	 * @author Christoph Strobl
	 */
	static class CycleGuard {

		private final Map<String, List<Path>> propertyTypeMap;

		CycleGuard() {
			this.propertyTypeMap = new LinkedHashMap<String, List<Path>>();
		}

		/**
		 * @param property The property to inspect
		 * @param path The path under which the property can be reached.
		 * @throws CyclicPropertyReferenceException in case a potential cycle is detected.
		 * @see Path#cycles(MongoPersistentProperty, String)
		 */
		void protect(MongoPersistentProperty property, String path) throws CyclicPropertyReferenceException {

			String propertyTypeKey = createMapKey(property);
			if (propertyTypeMap.containsKey(propertyTypeKey)) {

				List<Path> paths = propertyTypeMap.get(propertyTypeKey);

				for (Path existingPath : paths) {

					if (existingPath.cycles(property, path) && property.isEntity()) {
						paths.add(new Path(property, path));

						throw new CyclicPropertyReferenceException(property.getFieldName(), property.getOwner().getType(),
								existingPath.getPath());
					}
				}

				paths.add(new Path(property, path));
			} else {

				ArrayList<Path> paths = new ArrayList<Path>();
				paths.add(new Path(property, path));
				propertyTypeMap.put(propertyTypeKey, paths);
			}
		}

		private String createMapKey(MongoPersistentProperty property) {
			return property.getOwner().getType().getSimpleName() + ":" + property.getFieldName();
		}

		/**
		 * Path defines the property and its full path from the document root. <br />
		 * A {@link Path} with {@literal spring.data.mongodb} would be created for the property {@code Three.mongodb}.
		 * 
		 * <pre>
		 * <code>
		 * &#64;Document
		 * class One {
		 *   Two spring;
		 * }
		 * 
		 * class Two {
		 *   Three data;
		 * }
		 * 
		 * class Three {
		 *   String mongodb;
		 * }
		 * </code>
		 * </pre>
		 * 
		 * @author Christoph Strobl
		 */
		static class Path {

			private final MongoPersistentProperty property;
			private final String path;

			Path(MongoPersistentProperty property, String path) {

				this.property = property;
				this.path = path;
			}

			public String getPath() {
				return path;
			}

			/**
			 * Checks whether the given property is owned by the same entity and if it has been already visited by a subset of
			 * the current path. Given {@literal foo.bar.bar} cycles if {@literal foo.bar} has already been visited and
			 * {@code class Bar} contains a property of type {@code Bar}. The previously mentioned path would not cycle if
			 * {@code class Bar} contained a property of type {@code SomeEntity} named {@literal bar}.
			 * 
			 * @param property
			 * @param path
			 * @return
			 */
			boolean cycles(MongoPersistentProperty property, String path) {

				if (!property.getOwner().equals(this.property.getOwner())) {
					return false;
				}

				return path.equals(this.path) || path.contains(this.path + ".") || path.contains("." + this.path);
			}
		}
	}
	
}
