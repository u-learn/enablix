package com.enablix.trigger.lifecycle.contentgroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentGroupType;

@Component
public class DefaultContentGroupResolver extends SpringBackedAbstractFactory<ContentGroupSubsetResolver> implements ContentGroupResolver {

	@Override
	public Collection<ContentDataRecord> fetchContentGroupRecords(List<ContentDataRecord> execFocus,
			ContentGroupType contentGroupDef) {
		
		Map<Object, ContentDataRecord> contentRecords = new HashMap<>();
		
		for (ContentGroupSubsetResolver resolver : registeredInstances()) {
			
			List<ContentDataRecord> records = resolver.fetchContentGroupRecords(execFocus, contentGroupDef);
			
			for (ContentDataRecord record : records) {
				contentRecords.put(record.getRecord().get(ContentDataConstants.IDENTITY_KEY), record);
			}
		}
		
		return contentRecords.values();
	}

	@Override
	protected Class<ContentGroupSubsetResolver> lookupForType() {
		return ContentGroupSubsetResolver.class;
	}

}
