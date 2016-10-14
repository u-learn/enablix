package com.enablix.analytics.search.es;

import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class SearchHitToContentDataRefTxImpl implements SearchHitToContentDataRefTransformer {

	@Override
	public ContentDataRef transform(SearchHit searchHit, ContentTemplate template) {
		
		ContainerType container = TemplateUtil.findContainerForCollection(
									searchHit.getType(), template);
		
		String containerQId = container.getQualifiedId();
		
		Map<String, Object> source = searchHit.getSource();
		Object identity = source.get(ContentDataConstants.IDENTITY_KEY);
		
		ContentDataRef contentDataRef = null;
		
		if (identity instanceof String) {
			String contentTitle = ContentDataUtil.findPortalLabelValue(source, template, containerQId);
			contentDataRef = new ContentDataRef(template.getId(), 
								containerQId, (String) identity, contentTitle);	
		} else {
			throw new IllegalStateException("[identity] not of type string. Found [" 
						+ identity.getClass().getName() + "]");	
		}
		
		return contentDataRef;
	}

}
