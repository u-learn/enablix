package com.enablix.data.view.mongo.coll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataSegmentAttrFilter;
import com.enablix.data.view.mongo.AttributeMatcher;
import com.enablix.data.view.mongo.CollectionViewBuilder;

@Component
public class CollectionViewBuilderImpl implements CollectionViewBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionViewBuilderImpl.class);
	
	@Autowired
	private CollectionViewBuilderHelperRegistry checkerRegistry;
	
	@Override
	public boolean isCollectionVisible(String collectionName, DataSegment dataSegment) {
		
		boolean visible = false;
		
		CollectionViewBuilderHelper helper = checkerRegistry.getHelper(collectionName);
		
		if (helper != null) {
			visible = helper.isCollectionVisible(collectionName, dataSegment);
		} else {
			LOGGER.error("No collection view builder found for collection [{}]", collectionName);
		}
		
		return visible;
	}

	@Override
	public List<DataSegmentAttrFilter> createDataSegmentFilters(String collectionName, DataSegment dataSegment) {
		
		List<DataSegmentAttrFilter> filters = new ArrayList<>();
		
		CollectionViewBuilderHelper helper = checkerRegistry.getHelper(collectionName);
		
		if (helper != null) {
			
			filters = helper.createDataSegmentFilters(collectionName, dataSegment);
			
		} else {
			LOGGER.error("No collection view helper found for collection [{}]", collectionName);
			throw new IllegalStateException("No collection view builder helper found");
		}
		
		return filters;
	}

	@Override
	public AttributeMatcher<?> attributeMatcher(String collectionName) {
		
		CollectionViewBuilderHelper helper = checkerRegistry.getHelper(collectionName);
		
		if (helper == null) {
			LOGGER.error("No collection view helper found for collection [{}]", collectionName);
			throw new IllegalStateException("No collection view builder helper found");
		}
		
		return helper.attributeMatcher();
	}

}
