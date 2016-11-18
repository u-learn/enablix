package com.enablix.mongo.index;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.mongodb.core.index.IndexDirection;

/**
 * Mark a field to be indexed using MongoDB's indexing feature.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiTenantIndexed {

	/**
	 * If set to true reject all documents that contain a duplicate value for the indexed field.
	 * 
	 * @see http://docs.mongodb.org/manual/core/index-unique/
	 * @return
	 */
	boolean unique() default false;

	IndexDirection direction() default IndexDirection.ASCENDING;

	/**
	 * If set to true index will skip over any document that is missing the indexed field.
	 * 
	 * @see http://docs.mongodb.org/manual/core/index-sparse/
	 * @return
	 */
	boolean sparse() default false;

	/**
	 * @see http://docs.mongodb.org/manual/core/index-creation/#index-creation-duplicate-dropping
	 * @return
	 */
	boolean dropDups() default false;

	/**
	 * Index name. <br />
	 * <br />
	 * The name will only be applied as is when defined on root level. For usage on nested or embedded structures the
	 * provided name will be prefixed with the path leading to the entity. <br />
	 * <br />
	 * The structure below
	 * 
	 * <pre>
	 * <code>
	 * &#64;Document
	 * class Root {
	 *   Hybrid hybrid;
	 *   Nested nested;
	 * }
	 * 
	 * &#64;Document
	 * class Hybrid {
	 *   &#64;Indexed(name="index") String h1;
	 * }
	 * 
	 * class Nested {
	 *   &#64;Indexed(name="index") String n1;
	 * }
	 * </code>
	 * </pre>
	 * 
	 * resolves in the following index structures
	 * 
	 * <pre>
	 * <code>
	 * db.root.ensureIndex( { hybrid.h1: 1 } , { name: "hybrid.index" } )
	 * db.root.ensureIndex( { nested.n1: 1 } , { name: "nested.index" } )
	 * db.hybrid.ensureIndex( { h1: 1} , { name: "index" } )
	 * </code>
	 * </pre>
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * If set to {@literal true} then MongoDB will ignore the given index name and instead generate a new name. Defaults
	 * to {@literal false}.
	 * 
	 * @return
	 * @since 1.5
	 */
	boolean useGeneratedName() default false;

	/**
	 * If {@literal true} the index will be created in the background.
	 * 
	 * @see http://docs.mongodb.org/manual/core/indexes/#background-construction
	 * @return
	 */
	boolean background() default false;

	/**
	 * Configures the number of seconds after which the collection should expire. Defaults to -1 for no expiry.
	 * 
	 * @see http://docs.mongodb.org/manual/tutorial/expire-data/
	 * @return
	 */
	int expireAfterSeconds() default -1;
}
