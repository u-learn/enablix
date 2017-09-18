package com.enablix.core.api;

import java.util.Collection;
import java.util.List;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.QualityRuleType;

public interface TemplateFacade {

	ContentTemplate getTemplate();

	ContainerType getContainerDefinition(String containerQId);

	String getCollectionName(String containerQId);

	String getPortalLabelAttributeId(String containerQId);

	String getStudioLabelAttributeId(String containerQId);

	ContainerType getContainerForCollection(String collectionName);
	
	ContentItemType getDataSegmentAttrDefinition(String attrId);

	String getId();

	ContentItemType getContainerContentItemForDataSegmentAttr(
			String containerQId, String dataSegmentAttrId);

	Collection<String> getDataSegmentAttrIds();

	Collection<String> getAllCollectionNames();

	List<String> getApplicableQualityRules(String contentQId);

	QualityRuleType getQualityRule(String ruleId);

}