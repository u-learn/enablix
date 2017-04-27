package com.enablix.services.util.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.Assert;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataSegmentDefinitionType;
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
		initDataSegmentCache();
		initContainerCache();
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
				
				if (!TemplateUtil.isLinkedContainer(container)) {
					collectionNameContainerMap.put(collectionName, container);
				}
				
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
	
}
