package com.enablix.core.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.core.mongo.view.MongoDataView;

public class MongoDataViewContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDataViewContext.class);
	
	public static final ThreadLocal<MongoDataViewContext> THREAD_LOCAL_PROCESS_CONTEXT = new ThreadLocal<MongoDataViewContext>();

	public static void initialize(MongoDataView view) {
		
		MongoDataViewContext existingCtx = get();
		
		if (existingCtx == null) {
		
			MongoDataViewContext ctx = new MongoDataViewContext(view);
			THREAD_LOCAL_PROCESS_CONTEXT.set(ctx);
			
			LOGGER.trace("Initialized " + ctx);
			
			postInitialization(ctx);

		} else if (existingCtx.getView() != view) {
			throw new IllegalStateException("Mongo Data View context cannot be re-initialized");
		}
	}
	
	public static MongoDataViewContext get() {
		return THREAD_LOCAL_PROCESS_CONTEXT.get();
	}
	
	public static void clear() {
		THREAD_LOCAL_PROCESS_CONTEXT.remove();
		postDestroy();
	}
	
	private static void postInitialization(final MongoDataViewContext context) {
	}
	
	private static void postDestroy() {
	}
	
	private MongoDataView view;

	private MongoDataViewContext(MongoDataView view) {
		super();
		this.view = view;
	}

	public MongoDataView getView() {
		return view;
	}

	public void setView(MongoDataView view) {
		this.view = view;
	}

}
