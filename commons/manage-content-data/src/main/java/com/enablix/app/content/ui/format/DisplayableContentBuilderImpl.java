package com.enablix.app.content.ui.format;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.DisplayField;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.core.ui.DocRef;
import com.enablix.core.ui.FieldValue;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class DisplayableContentBuilderImpl implements DisplayableContentBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DisplayableContentBuilderImpl.class);

	@Value("${site.url.container.instance}")
	private String containerInstanceUrl;
	
	@Autowired
	private DisplayFieldBuilder fieldBuilder;
	
	@Override
	public DisplayableContent build(ContentTemplate template, ContentDataRecord record, DisplayContext ctx) {
		
		String containerQId = record.getContainerQId();
		Map<String, Object> contentRecord = record.getRecord();
		
		ContainerType containerDef = TemplateUtil.findContainer(template.getDataDefinition(), containerQId );
		if (TemplateUtil.isLinkedContainer(containerDef)) {
			containerDef = TemplateUtil.findContainer(template.getDataDefinition(), containerDef.getLinkContainerQId());
		}
		
		DisplayableContent dispContent = new DisplayableContent();
		dispContent.setContainerQId(record.getContainerQId());
		dispContent.setContainerLabel(containerDef.getLabel());
		dispContent.setRecordIdentity((String) record.getRecord().get(ContentDataConstants.IDENTITY_KEY));
		dispContent.setTitle(ContentDataUtil.findStudioLabelValue(contentRecord, template, containerQId));
		dispContent.setPortalUrl(getContentInstanceAccessUrl(dispContent.getContainerQId(), dispContent.getRecordIdentity()));
		for (ContentItemType fieldDef : containerDef.getContentItem()) {
			
			DisplayField<?> field = fieldBuilder.build(fieldDef, template, contentRecord, ctx);
			
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
	
	private String getContentInstanceAccessUrl(String containerQId, String contentIdentity) {
		
		if (StringUtil.isEmpty(containerQId) || StringUtil.isEmpty(contentIdentity)) {
			LOGGER.error("Error creating access url. [containerQId={}, contentIdentity={}]", containerQId, contentIdentity);
			return "";
		} 
		
		return EnvPropertiesUtil.getProperties().getServerUrl() + 
				containerInstanceUrl.replaceAll(":containerQId", containerQId)
									.replaceAll(":contentIdentity", contentIdentity);
	}

}
