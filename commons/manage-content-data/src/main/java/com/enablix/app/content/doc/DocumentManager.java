package com.enablix.app.content.doc;

import java.io.IOException;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;

public interface DocumentManager {

	DocumentMetadata save(Document<?> doc) throws IOException;
	
	Document<DocumentMetadata> load(String docIdentity);
	
}
