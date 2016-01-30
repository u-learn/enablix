package com.enablix.app.content.doc.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.app.content.doc.DocStoreConfigMetadata;
import com.enablix.app.content.doc.DocStoreConfigMetadataProvider;
import com.enablix.util.data.loader.DataFileProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileDocStoreConfigMetadataProvider implements DocStoreConfigMetadataProvider, DataFileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonFileDocStoreConfigMetadataProvider.class);
	
	private Map<String, DocStoreConfigMetadata> cache = new LinkedHashMap<>();
	
	@Override
	public void process(File dataFile) {
		
		try {
		
			DocStoreConfigMetadata docStoreConfigMetadata = new ObjectMapper().readValue(dataFile, DocStoreConfigMetadata.class);
			cache.put(docStoreConfigMetadata.getStoreTypeCode(), docStoreConfigMetadata);
			
		} catch (IOException e) {
			LOGGER.error("Error reading file [" + dataFile.getAbsolutePath() +"]", e);
		}
		
	}

	@Override
	public Collection<DocStoreConfigMetadata> getDocStoresConfigMetadata() {
		return cache.values();
	}

}
