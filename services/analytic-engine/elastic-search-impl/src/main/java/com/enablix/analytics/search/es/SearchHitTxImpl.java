package com.enablix.analytics.search.es;

import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.ContentDataUtil;

@Component
public class SearchHitTxImpl implements SearchHitTransformer {

	@Override
	public ContentDataRef toContentDataRef(SearchHit searchHit, TemplateFacade templateWrapper) {
		
		ContentTemplate template = templateWrapper.getTemplate();
		ContainerType container = templateWrapper.getContainerForCollection(searchHit.getType());
		
		String containerQId = container.getQualifiedId();
		
		Map<String, Object> source = searchHit.getSource();
		Object identity = source.get(ContentDataConstants.IDENTITY_KEY);
		
		ContentDataRef contentDataRef = null;
		
		if (identity instanceof String) {
			String contentTitle = ContentDataUtil.findPortalLabelValue(source, templateWrapper, containerQId);
			contentDataRef = ContentDataRef.createContentRef(template.getId(), 
								containerQId, (String) identity, contentTitle);	
		} else {
			throw new IllegalStateException("[identity] not of type string. Found [" 
						+ identity.getClass().getName() + "]");	
		}
		
		return contentDataRef;
	}

	@Override
	public ContentDataRecord toContentDataRecord(SearchHit searchHit, TemplateFacade templateWrapper) {
		
		ContentTemplate template = templateWrapper.getTemplate();
		ContainerType container = templateWrapper.getContainerForCollection(searchHit.getType());
		
		ContentDataRecord contentDataRef = new ContentDataRecord(
				template.getId(), container.getQualifiedId(), searchHit.getSource());	
		
		return contentDataRef;
	}

}
