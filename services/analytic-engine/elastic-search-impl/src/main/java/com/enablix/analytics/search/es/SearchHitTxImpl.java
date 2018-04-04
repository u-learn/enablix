package com.enablix.analytics.search.es;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
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
		
		ContentDataRecordExt contentDataRef = new ContentDataRecordExt(
				template.getId(), container.getQualifiedId(), searchHit.getSource());	
		
		Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
		
		if (CollectionUtil.isNotEmpty(highlightFields)) {
		
			for (HighlightField hlField : highlightFields.values()) {
				Text[] fragments = hlField.fragments();
				if (fragments.length > 0) {
					contentDataRef.highlights.put(hlField.getName(), fragments[0].toString());
				}
			}
		}
		
		return contentDataRef;
	}
	
	private static class ContentDataRecordExt extends ContentDataRecord {
		
		private Map<String, String> highlights = new HashMap<>();

		@SuppressWarnings("unused")
		private ContentDataRecordExt() {
			// for ORM
		}
		
		public ContentDataRecordExt(String templateId, String containerQId, Map<String, Object> record) {
			super(templateId, containerQId, record);
		}

		@SuppressWarnings("unused")
		public Map<String, String> getHighlights() {
			return highlights;
		}

		@SuppressWarnings("unused")
		public void setHighlights(Map<String, String> highlights) {
			this.highlights = highlights;
		}
		
	}

}
