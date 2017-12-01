package com.enablix.services.util.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.Assert;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerRefListType;
import com.enablix.core.commons.xsdtopojo.ContainerRefType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataSegmentDefinitionType;
import com.enablix.core.commons.xsdtopojo.QualityRuleConfigType;
import com.enablix.core.commons.xsdtopojo.QualityRuleType;
import com.enablix.core.commons.xsdtopojo.QualityRulesType;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.ContainerVisitor;
import com.enablix.services.util.template.walker.TemplateContainerWalker;

public class TemplateWrapper implements TemplateFacade {

	private ContentTemplate template;
	
	private Map<String, ContainerType> containerQIdMap;
	private Map<String, String> qIdCollectionMap;
	private Map<String, String> containerPortalLabelAttrIdMap;
	private Map<String, String> containerStudioLabelAttrIdMap;
	private Map<String, ContainerType> collectionNameContainerMap;
	private Map<String, ContentItemType> dataSegmentAttrIdMap;
	private Map<String, ContentItemType> containerDataSegmentAttrMap;
	private Map<String, QualityRuleType> qualityRuleConfigMap;
	private Map<String, List<String>> containerQualityRuleIdMap;
	
	private List<ContainerType> bizDimContainers;
	private List<ContainerType> bizContentContainers;
 	private Set<String> docContainers;
	private Set<String> richTextContainers;

	public TemplateWrapper(ContentTemplate template) {
		super();
		this.template = template;
		this.initCache();
	}

	/* (non-Javadoc)
	 * @see com.enablix.services.util.template.TemplateFacade#getTemplate()
	 */
	@Override
	public ContentTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ContentTemplate template) {
		this.template = template;
	}
	
	private void initCache() {
		Assert.notNull(this.template, "Content template can not be null");
		initQualityRuleCache();
		initDataSegmentCache();
		initContainerCache();
	}
	
	private void initQualityRuleCache() {
		
		this.qualityRuleConfigMap = new HashMap<>();
		
		QualityRuleConfigType qualityRuleConfig = template.getQualityRuleConfig();
		
		if (qualityRuleConfig != null) {
		
			QualityRulesType rules = qualityRuleConfig.getQualityRules();
			
			if (rules != null) {
				for (QualityRuleType rule : rules.getRule()) {
					qualityRuleConfigMap.put(rule.getId(), rule);
				}
			}
		}
		
	}

	private void initDataSegmentCache() {
		
		this.dataSegmentAttrIdMap = new HashMap<String, ContentItemType>();
		
		DataSegmentDefinitionType dsDefinition = this.template.getDataSegmentDefinition();
		
		if (dsDefinition != null) {
			for (ContentItemType dsAttr : dsDefinition.getSegmentAttr()) {
				dataSegmentAttrIdMap.put(dsAttr.getId(), dsAttr);
			}
		}
	}

	private void initContainerCache() {
		
		this.containerQIdMap = new HashMap<>();
		this.qIdCollectionMap = new HashMap<>();
		this.containerPortalLabelAttrIdMap = new HashMap<>();
		this.containerStudioLabelAttrIdMap = new HashMap<>();
		this.collectionNameContainerMap = new HashMap<>();
		this.containerDataSegmentAttrMap = new HashMap<>();
		this.containerQualityRuleIdMap = new HashMap<>();
		
		this.bizDimContainers = new ArrayList<>();
		this.bizContentContainers = new ArrayList<>();
		this.docContainers = new HashSet<>();
		this.richTextContainers = new HashSet<>();
		
		
		TemplateContainerWalker walker = new TemplateContainerWalker(this.template);
		walker.walk(new ContainerVisitor() {
			
			@Override
			public void visit(ContainerType container) {
				
				String containerQId = container.getQualifiedId();
				
				containerQIdMap.put(containerQId, container);
				
				String collectionName = TemplateUtil.resolveCollectionName(template, containerQId);
				qIdCollectionMap.put(containerQId, collectionName);
				
				containerPortalLabelAttrIdMap.put(containerQId, 
						TemplateUtil.getPortalLabelAttributeId(template, containerQId));
				
				containerStudioLabelAttrIdMap.put(containerQId, 
						TemplateUtil.getStudioLabelAttributeId(template, containerQId));
				
				if (!TemplateUtil.isLinkedContainer(container) && container.isReferenceable()) {
					collectionNameContainerMap.put(collectionName, container);
				}
				
				if (container.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_CONTENT) {
					bizContentContainers.add(container);
				}
				
				if (container.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_DIMENSION) {
					bizDimContainers.add(container);
				}
				
				populateContainerType(container);
				
				initQualityRuleMapping(container);
				
				for (Entry<String, ContentItemType> segmentAttr : dataSegmentAttrIdMap.entrySet()) {
					
					ContentItemType containerContentItem = 
							TemplateUtil.findMatchingContentItem(container, segmentAttr.getValue());
					
					if (containerContentItem != null) {
						containerDataSegmentAttrMap.put(
								containerDataSegmentAttrKey(container.getQualifiedId(), segmentAttr.getKey()), 
								containerContentItem);
					}
				}
			}
			
			private void populateContainerType(ContainerType container) {
				
				container.getContentItem().forEach((contItem) -> {
					
					if (contItem.getType() == ContentItemClassType.DOC) {
						docContainers.add(container.getQualifiedId());
					}
					
					if (contItem.getType() == ContentItemClassType.RICH_TEXT) {
						richTextContainers.add(container.getQualifiedId());
					}
				});
			}

			private void initQualityRuleMapping(ContainerType container) {
				
				for (QualityRuleType rule : qualityRuleConfigMap.values()) {
					
					boolean ruleApplicable = true;
					
					ContainerRefListType excludeContainerQIds = rule.getExclude();
					ContainerRefListType includeContainerQIds = rule.getInclude();
					
					if (excludeContainerQIds != null) {
						
						if (isContainerInList(container, excludeContainerQIds)) {
							ruleApplicable = false;
						}
						
					} else if (includeContainerQIds != null) {
						
						if (!isContainerInList(container, includeContainerQIds)) {
							ruleApplicable = false;
						}
					}
					
					if (ruleApplicable) {
						CollectionUtil.addToMappedListValue(container.getQualifiedId(), 
								rule.getId(), containerQualityRuleIdMap, () -> new ArrayList<>());
					}
				}
			}

			private boolean isContainerInList(ContainerType container, ContainerRefListType excludeContainerQIds) {
				
				for (ContainerRefType crt : excludeContainerQIds.getContainer()) {
					if (container.getQualifiedId().equals(crt.getQualifiedId())) {
						return true;
					}
				}
				
				return false;
			}
			
		});
		
	}
	
	private String containerDataSegmentAttrKey(String containerQId, String dataSegmentAttrId) {
		return containerQId + "::" + dataSegmentAttrId;
	}
	
	/* (non-Javadoc)
	 * @see com.enablix.services.util.template.TemplateFacade#getContainerDefinition(java.lang.String)
	 */
	@Override
	public ContainerType getContainerDefinition(String containerQId) {
		return containerQIdMap.get(containerQId);
	}
	
	/* (non-Javadoc)
	 * @see com.enablix.services.util.template.TemplateFacade#getCollectionName(java.lang.String)
	 */
	@Override
	public String getCollectionName(String containerQId) {
		return qIdCollectionMap.get(containerQId);
	}
	
	/* (non-Javadoc)
	 * @see com.enablix.services.util.template.TemplateFacade#getPortalLabelAttributeId(java.lang.String)
	 */
	@Override
	public String getPortalLabelAttributeId(String containerQId) {
		return containerPortalLabelAttrIdMap.get(containerQId);
	}

	/* (non-Javadoc)
	 * @see com.enablix.services.util.template.TemplateFacade#getStudioLabelAttributeId(java.lang.String)
	 */
	@Override
	public String getStudioLabelAttributeId(String containerQId) {
		return containerStudioLabelAttrIdMap.get(containerQId);
	}
	
	/* (non-Javadoc)
	 * @see com.enablix.services.util.template.TemplateFacade#getContainerForCollection(java.lang.String)
	 */
	@Override
	public ContainerType getContainerForCollection(String collectionName) {
		return collectionNameContainerMap.get(collectionName);
	}

	/* (non-Javadoc)
	 * @see com.enablix.services.util.template.TemplateFacade#getId()
	 */
	@Override
	public String getId() {
		return template.getId();
	}

	@Override
	public ContentItemType getDataSegmentAttrDefinition(String attrId) {
		return dataSegmentAttrIdMap.get(attrId);
	}
	
	@Override
	public ContentItemType getContainerContentItemForDataSegmentAttr(String containerQId, String dataSegmentAttrId) {
		
		ContainerType containerDef = getContainerDefinition(containerQId);
		
		if (TemplateUtil.isLinkedContainer(containerDef)) {
			containerQId = containerDef.getLinkContainerQId();
		}
		
		return containerDataSegmentAttrMap.get(containerDataSegmentAttrKey(containerQId, dataSegmentAttrId));
	}

	@Override
	public Collection<String> getDataSegmentAttrIds() {
		return dataSegmentAttrIdMap.keySet();
	}

	@Override
	public Collection<String> getAllCollectionNames() {
		return collectionNameContainerMap.keySet();
	}

	@Override
	public List<String> getApplicableQualityRules(String contentQId) {
		return containerQualityRuleIdMap.get(contentQId);
	}

	@Override
	public QualityRuleType getQualityRule(String ruleId) {
		return qualityRuleConfigMap.get(ruleId);
	}

	@Override
	public boolean isDocContainer(ContainerType container) {
		return this.docContainers.contains(container.getQualifiedId());
	}

	@Override
	public boolean isTextContainer(ContainerType container) {
		return this.richTextContainers.contains(container.getQualifiedId());
	}

	@Override
	public List<ContainerType> getBizContentContainers() {
		return this.bizContentContainers;
	}

	@Override
	public List<ContainerType> getBizDimContainers() {
		return this.bizDimContainers;
	}
	
}
