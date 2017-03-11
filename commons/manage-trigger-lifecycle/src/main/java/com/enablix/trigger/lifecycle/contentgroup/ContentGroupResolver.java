package com.enablix.trigger.lifecycle.contentgroup;

import java.util.Collection;
import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentGroupType;

public interface ContentGroupResolver {

	Collection<ContentDataRecord> fetchContentGroupRecords(List<ContentDataRecord> execFocus, ContentGroupType contentGroupDef);
	
}
