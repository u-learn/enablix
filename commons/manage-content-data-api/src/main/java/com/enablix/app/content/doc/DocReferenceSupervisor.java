package com.enablix.app.content.doc;

import com.enablix.commons.dms.api.DocumentMetadata;

public interface DocReferenceSupervisor {

	void updateDocMetadataAttr(DocumentMetadata docMetadata, String attrId, Object attrValue);

	boolean checkReferenceRecordExists(DocumentMetadata docMetadata);
	
}
