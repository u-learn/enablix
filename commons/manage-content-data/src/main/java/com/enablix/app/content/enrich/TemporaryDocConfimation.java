package com.enablix.app.content.enrich;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.TemplateUtil;

@Component
public class TemporaryDocConfimation implements ContentEnricher {

	@Autowired
	private DocumentManager docManager;
	
	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, ContentTemplate contentTemplate) {
		
		String parentIdentity = updateCtx.parentIdentity();
		String contentQId = updateCtx.contentQId();
		String contentIdentity = (String) content.get(ContentDataConstants.IDENTITY_KEY);
		
		ContainerType containerDef = TemplateUtil.findContainer(contentTemplate.getDataDefinition(), contentQId);
		
		for (ContentItemType itemDef : containerDef.getContentItem()) {

			if (itemDef.getType() == ContentItemClassType.DOC) {
			
				Object object = content.get(itemDef.getId());
				
				if (object != null && object instanceof Map) {
				
					Map<?,?> docMap = (Map<?,?>) object;
					Object tempDoc = docMap.get(ContentDataConstants.DOC_TEMPORARY_ATTR);
					
					if (tempDoc != null && tempDoc instanceof Boolean && ((Boolean) tempDoc)) {
						
						String docIdentity = (String) docMap.get(ContentDataConstants.IDENTITY_KEY);
						DocumentMetadata docMd = docManager.getDocumentMetadata(docIdentity);
						
						try {
							
							if (!StringUtil.isEmpty(parentIdentity)) {
								docMd = docManager.attachUsingContainerInfo(docMd, contentQId, parentIdentity);
							} else {
								docMd = docManager.attachUsingContainerInfo(docMd, contentQId, contentIdentity);
							}
							
							content.put(itemDef.getId(), JsonUtil.beanToMap(docMd));
							
						} catch (IOException e) {
							throw new RuntimeException("Unable to attach document", e);
						}
						
					}
				}
			}
		}
	}

}
