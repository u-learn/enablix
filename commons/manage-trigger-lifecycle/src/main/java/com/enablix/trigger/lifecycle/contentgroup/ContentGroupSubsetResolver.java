package com.enablix.trigger.lifecycle.contentgroup;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentGroupType;
import com.enablix.core.mongo.view.MongoDataView;

public interface ContentGroupSubsetResolver {

	List<ContentDataRecord> fetchContentGroupRecords(
			List<ContentDataRecord> execFocus, ContentGroupType contentGroupDef, MongoDataView view);
	
}
