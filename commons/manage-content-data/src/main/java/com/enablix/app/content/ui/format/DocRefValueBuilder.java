package com.enablix.app.content.ui.format;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.DocRef;

@Component
public class DocRefValueBuilder implements FieldValueBuilder<DocRef, Map<String, Object>> {

	@Override
	public DocRef build(ContentItemType fieldDef, Map<String, Object> fieldValue, ContentTemplate template) {
		DocRef doc = new DocRef();
		doc.setName((String) fieldValue.get(ContentDataConstants.DOC_NAME_ATTR));
		doc.setDocIdentity((String) fieldValue.get(ContentDataConstants.IDENTITY_KEY)); 
		return doc;
	}

	@Override
	public boolean canHandle(ContentItemType fieldDef) {
		return fieldDef.getType() == ContentItemClassType.DOC;
	}

}
