package com.enablix.app.content.doc.web;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.doc.DocStoreConfigMetadata;
import com.enablix.app.content.doc.DocStoreConfigMetadataProvider;

@RestController
@RequestMapping("docstore")
public class DocStoreConfigController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocStoreConfigController.class);
	
	@Autowired
	private DocStoreConfigMetadataProvider docstoreConfigMetadataProvider;
	
	@RequestMapping(value = "/configmetadata", method = RequestMethod.GET, produces = "application/json")
	public Collection<DocStoreConfigMetadata> fetchDocStoresConfigMetadata() {
		LOGGER.debug("Fetching doc store config metadata");
		return docstoreConfigMetadataProvider.getDocStoresConfigMetadata();
	}

	
}
