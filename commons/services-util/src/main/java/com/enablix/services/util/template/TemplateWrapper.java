package com.enablix.services.util.template;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.ContainerVisitor;
import com.enablix.services.util.template.walker.TemplateContainerWalker;

public class TemplateWrapper {

	private ContentTemplate template;
	
	private Map<String, ContainerType> containerQIdMap;
	private Map<String, String> qIdCollectionMap;
	private Map<String, String> containerPortalLabelAttrIdMap;
	private Map<String, String> containerStudioLabelAttrIdMap;
	private Map<String, ContainerType> collectionNameContainerMap;

	public TemplateWrapper(ContentTemplate template) {
		super();
		this.template = template;
		this.initCache();
	}

	public ContentTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ContentTemplate template) {
		this.template = template;
	}
	
	private void initCache() {
		Assert.notNull(this.template, "Content template can not be null");
		initContainerCache();
	}
	
	private void initContainerCache() {
		
		this.containerQIdMap = new HashMap<>();
		this.qIdCollectionMap = new HashMap<>();
		this.containerPortalLabelAttrIdMap = new HashMap<>();
		this.containerStudioLabelAttrIdMap = new HashMap<>();
		this.collectionNameContainerMap = new HashMap<>();
		
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
				
				collectionNameContainerMap.put(collectionName, container);
			}
		});
		
	}
	
	public ContainerType getContainerDefinition(String containerQId) {
		return containerQIdMap.get(containerQId);
	}
	
	public String getCollectionName(String containerQId) {
		return qIdCollectionMap.get(containerQId);
	}
	
	public String getPortalLabelAttributeId(String containerQId) {
		return containerPortalLabelAttrIdMap.get(containerQId);
	}

	public String getStudioLabelAttributeId(String containerQId) {
		return containerStudioLabelAttrIdMap.get(containerQId);
	}
	
	public ContainerType getContainerForCollection(String collectionName) {
		return collectionNameContainerMap.get(collectionName);
	}

	public String getId() {
		return template.getId();
	}
	
}
