package com.enablix.services.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.content.EmbeddedUrl;
import com.enablix.core.domain.content.ContentAssociation;

public class ContentDataUtil {

	public static String findParentIdentityFromAssociation(Map<String, Object> record) {
		
		String parentIdentity = null;
		
		if (record != null) {
			
			Object associations = record.get(ContentDataConstants.ASSOCIATIONS_KEY);
			
			if (associations != null && associations instanceof Map) {
			
				Map<?,?> mapAssoc = (Map<?,?>) associations;
				Object parentAssocList = mapAssoc.get(ContentDataConstants.PARENT_ASSOCIATION);
				
				if (parentAssocList != null && parentAssocList instanceof Collection) {
				
					Collection<?> parents = (Collection<?>) parentAssocList;
					if (!parents.isEmpty()) {
						
						Object parentAssoc = parents.iterator().next();
						
						if (parentAssoc instanceof ContentAssociation) {
							parentIdentity = ((ContentAssociation) parentAssoc).getRecordIdentity();
							
						} else if (parentAssoc instanceof Map<?,?>) {
							parentIdentity = (String) ((Map<?,?>) parentAssoc).get(
											ContentDataConstants.RECORD_IDENTITY_KEY);
						}
					}
				}
			}
		}
		
		return parentIdentity;
	}


	public static String findPortalLabelValue(Map<String, Object> record, 
			TemplateFacade template, String qId, boolean defaultToFirstItemIfNotDefined) {
		
		String portalLabelAttributeId = template.getPortalLabelAttributeId(qId);
		
		if (!StringUtil.hasText(portalLabelAttributeId) && defaultToFirstItemIfNotDefined) {
			
			ContainerType containerDef = template.getContainerDefinition(qId);
			
			if (containerDef != null) {
				ContentItemType contentItem = containerDef.getContentItem().get(0);
				portalLabelAttributeId = contentItem.getId();
			}
		}
		
		return findLabelAttributeValue(record, template, qId, portalLabelAttributeId);
	}
	
	public static String findPortalLabelValue(
			Map<String, Object> record, TemplateFacade template, String qId) {
		return findPortalLabelValue(record, template, qId, false);
	}
	
	public static String findStudioLabelValue(
			Map<String, Object> record, TemplateFacade template, String qId) {
		
		String studioLabelAttributeId = template.getStudioLabelAttributeId(qId);
		
		return findLabelAttributeValue(record, template, qId, studioLabelAttributeId);
	}
	
	public static String findLabelAttributeValue(Map<String, Object> record, 
			TemplateFacade template, String qId, String labelAttrId) {
		
		String labelAttributeValue = null;
		ContainerType containerDef = template.getContainerDefinition(qId);
		
		if (labelAttrId != null) {
			
			Object labelAttribute = record.get(labelAttrId);
			
			if (TemplateUtil.isLinkedContainer(containerDef)) {
				containerDef = template.getContainerDefinition(containerDef.getLinkContainerQId());
			}
			
			labelAttributeValue = contentItemToString(labelAttribute, containerDef, labelAttrId);
		}
		
		return labelAttributeValue == null ? containerDef.getLabel() : labelAttributeValue;
	}
	
	public static String findDocIdentity(Map<String, Object> record, ContainerType containerDef) {
		
		String docIdentity = null;
		
		for (ContentItemType itemType : containerDef.getContentItem()) {
			
			if (ContentItemClassType.DOC.equals(itemType.getType())) {
				
				Object doc = record.get(itemType.getId());
				if (doc != null && doc instanceof Map<?,?>) {
					Map<?,?> docDetails = (Map<?,?>) doc;
					docIdentity = String.valueOf(
							docDetails.get(ContentDataConstants.IDENTITY_KEY));
				}
				
				break;
			}
		}
		
		return docIdentity;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> findDocRecord(Map<String, Object> record, 
			ContainerType containerDef, TemplateFacade template) {
		
		Map<String, Object> docDetails = null;
		
		if (TemplateUtil.isLinkedContainer(containerDef)) {
			containerDef = template.getContainerDefinition(containerDef.getLinkContainerQId());
		}
		
		for (ContentItemType itemType : containerDef.getContentItem()) {
			
			if (ContentItemClassType.DOC.equals(itemType.getType())) {
				
				Object doc = record.get(itemType.getId());
				if (doc != null && doc instanceof Map<?,?>) {
					docDetails = (Map<String, Object>) doc;
				}
				
				break;
			}
		}
		
		return docDetails;
	}

	private static String contentItemToString(Object labelAttribute, ContainerType containerDef,
			String portalLabelAttributeId) {
		
		for (ContentItemType itemType : containerDef.getContentItem()) {
		
			if (itemType.getId().equals(portalLabelAttributeId)) {
				return ContentItemToStringTransformer.convert(labelAttribute, itemType);
			}
		}
		
		return null;
	}
	
	
	public static ContentDataRef contentDataToRef(
			Map<String, Object> contentData, TemplateFacade template, String containerQId) {
		
		Object identity = contentData.get(ContentDataConstants.IDENTITY_KEY);
		
		ContentDataRef contentDataRef = null;
		
		if (identity instanceof String) {
			String contentTitle = ContentDataUtil.findPortalLabelValue(contentData, template, containerQId);
			contentDataRef = ContentDataRef.createContentRef(template.getTemplate().getId(), 
								containerQId, (String) identity, contentTitle);	
		} else {
			throw new IllegalStateException("[identity] not of type string. Found [" 
						+ identity.getClass().getName() + "]");	
		}
		
		return contentDataRef;
	}
	
	public static Date getContentCreatedAt(Map<String, Object> contentData) {
		return (Date) contentData.get(ContentDataConstants.CREATED_AT_KEY);
	}
	
	public static Date getContentModifiedAt(Map<String, Object> contentData) {
		return (Date) contentData.get(ContentDataConstants.MODIFIED_AT_KEY);
	}
	
	public static String getRecordIdentity(Map<String, Object> contentData) {
		return (String) contentData.get(ContentDataConstants.IDENTITY_KEY);
	}
	
	public static String getRecordId(Map<String, Object> contentData) {
		return String.valueOf(contentData.get(ContentDataConstants.RECORD_ID_KEY));
	}
	
	public static List<String> checkAndConvertToIdOrIdentityCollection(Object value) {
		
		List<String> listValue = null;
		
		if (value instanceof Collection) {
			
			Collection<?> collValue = (Collection<?>) value;
			
			listValue = new ArrayList<>();
			
			for (Object collItem : collValue) {
			
				if (collItem instanceof String) {
					
					listValue.add((String) collItem);
					
				} else if (collItem instanceof Map<?, ?>) {
					
					Map<?, ?> mapCollItem = (Map<?, ?>) collItem;
					Object itemId = mapCollItem.get(ContentDataConstants.ID_FLD_KEY);
					
					if (itemId != null) {
					
						listValue.add(String.valueOf(itemId));
						
					} else {
						
						Object itemIdentity = mapCollItem.get(ContentDataConstants.IDENTITY_KEY);
						if (itemIdentity != null) {
							listValue.add(String.valueOf(itemIdentity));
						}
					}
				}
			}
		}
		
		return listValue;
	}

	
	public static List<EmbeddedUrl> getEmbeddedUrls(ContentDataRecord record) {
		return getEmbeddedUrls(record.getRecord());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<EmbeddedUrl> getEmbeddedUrls(Map<String, Object> record) {
		
		List<EmbeddedUrl> urls = new ArrayList<>();
		
		Object embedUrls = record.get(ContentDataConstants.CONTENT_URLS_KEY);
		
		if (embedUrls != null && embedUrls instanceof Collection) {
		
			Collection<?> urlColl = (Collection<?>) embedUrls;
			for (Object embedUrl : urlColl) {
			
				if (embedUrl instanceof EmbeddedUrl) {
				
					urls.add((EmbeddedUrl) embedUrl);
				
				} else if (embedUrl instanceof Map) {
					
					EmbeddedUrl bean = JsonUtil.jsonToObject((Map) embedUrl, EmbeddedUrl.class);
					if (bean != null) {
						urls.add(bean);
					}
				}
			}
		}
		
		return urls;
	}


	public static boolean isDocContentTypeImage(Map<String, Object> docRecord) {
		if (docRecord != null) {
			String contentType = (String) docRecord.get(ContentDataConstants.CONTENT_TYPE_KEY);
			return isImageContentType(contentType);
		}
		return false;
	}


	public static boolean isImageContentType(String contentType) {
		if (StringUtil.hasText(contentType)) {
			return contentType.toLowerCase().contains("image");
		}
		return false;
	}


	public static String getContentModifiedBy(Map<String, Object> contentData) {
		return (String) contentData.get(ContentDataConstants.MODIFIED_BY_KEY);
	}
	
	public static String getContentModifiedByName(Map<String, Object> contentData) {
		return (String) contentData.get(ContentDataConstants.MODIFIED_BY_NAME_KEY);
	}

}
