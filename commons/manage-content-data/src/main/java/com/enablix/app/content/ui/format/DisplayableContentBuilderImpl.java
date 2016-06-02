package com.enablix.app.content.ui.format;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataUtil;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.TemplateUtil;

@Component
public class DisplayableContentBuilderImpl implements DisplayableContentBuilder {

	@Autowired
	private DisplayFieldBuilder fieldBuilder;
	
	@Override
	public DisplayableContent build(ContentTemplate template, ContentDataRecord record) {
		
		String containerQId = record.getContainerQId();
		Map<String, Object> contentRecord = record.getRecord();
		
		ContainerType containerDef = TemplateUtil.findContainer(template.getDataDefinition(), containerQId );
		
		DisplayableContent dispContent = new DisplayableContent();
		dispContent.setContainerQId(record.getContainerQId());
		dispContent.setRecordIdentity((String) record.getRecord().get(ContentDataConstants.IDENTITY_KEY));
		dispContent.setTitle(ContentDataUtil.findStudioLabelValue(contentRecord, template, containerQId));
		
		for (ContentItemType fieldDef : containerDef.getContentItem()) {
			
			DisplayField<?> field = fieldBuilder.build(fieldDef, template, contentRecord);
			
			if (field != null) {
			
				dispContent.addField(field);
				
				FieldValue fieldVal = field.getValue();
				if (fieldVal instanceof DocRef) {
					dispContent.setDoc((DocRef) fieldVal);
				}
			}
		}
		
		return dispContent;
	}

}
