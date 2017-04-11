package com.enablix.app.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.DefaultContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContentDataPathResolverImpl implements ContentDataPathResolver {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private ContentLabelResolver labelResolver = new DefaultContentLabelResolver();
	
	@Override
	public String resolveContentDataPath(String templateId, String containerQId,
			String containerInstanceIdentity, String containerInstanceTitle) {
		NavigableContent navContent = createNavigableContent(templateId, containerQId, 
				containerInstanceIdentity, containerInstanceTitle);
		return navContent == null ? "" : navContent.toPath("/");
	}

	private NavigableContent createNavigableContent(String templateId, String containerQId,
			String containerInstanceIdentity, String itemTitle) {
		ContentDataRef dataRef = ContentDataRef.createContentRef(templateId, containerQId, containerInstanceIdentity, itemTitle);
		NavigableContent navContent = navContentBuilder.build(dataRef, labelResolver);
		return navContent;
	}

	@Override
	public String resolveContentParentDataPath(String templateId, String containerQId, 
			String containerInstanceIdentity, String containerInstanceTitle) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		ContainerType containerDef = template.getContainerDefinition(containerQId);
		
		if (containerDef == null) {
			throw new IllegalArgumentException("Invalid container id [" + containerQId + "]");
		}
		
		NavigableContent navContent = createNavigableContent(templateId, containerQId, 
				containerInstanceIdentity, containerInstanceTitle);
		
		// remove the last node as that is the current node
		removeLastContent(navContent, null);
		
		return navContent == null ? "" : navContent.toPath("/");
	}
	
	private void removeLastContent(NavigableContent navContent, NavigableContent parent) {
		
		if (navContent != null) {
			
			if (parent != null) {
			
				if (navContent.getNext() == null) {
					// this is the last node, hence remove it from the parent node
					parent.setNext(null);
				}
				
			} else {
				
				if (navContent.getNext() != null) {
					removeLastContent(navContent.getNext(), navContent);
				}
			}
			
		}
		
	}

	@Override
	public String addContainerLabelToPath(String templateId, String containerQId, String currentPath) {
		
		ContentTemplate template = templateMgr.getTemplate(templateId);
		
		ContainerType containerDef = TemplateUtil.findContainer(
				template.getDataDefinition(), containerQId);

		if (!StringUtil.isEmpty(containerDef.getLabel())) {
		
			if (!StringUtil.isEmpty(currentPath)) {
				currentPath += "/" + containerDef.getLabel();
			} else {
				currentPath = containerDef.getLabel();
			}
		}
		
		return currentPath;
	}
	
	@Override
	public String appendPath(String currentPath, String addPath) {
		return currentPath + "/" + addPath;
	}

	@Override
	public String resolveContainerPath(String templateId, String containerQId) {
		
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		ContainerPathEntry pathEntry = createParentPathEntry(containerQId, template, null);
		
		return pathEntry == null ? "" : pathEntry.getPath("/");
	}
	
	private ContainerPathEntry createParentPathEntry(String containerQId, 
			TemplateFacade template, ContainerPathEntry child) {
		
		ContainerPathEntry pathEntry = null;
		
		if (StringUtil.hasText(containerQId)) {
			
			String containerLabel = getContainerLabel(containerQId, template);
			
			if (child == null) {
				pathEntry = new ContainerPathEntry(containerLabel);
			} else {
				pathEntry = child.addParent(containerLabel);
			}
			
			createParentPathEntry(QIdUtil.getParentQId(containerQId), template, pathEntry);
		}
		
		return pathEntry;
	}
	
	private static class ContainerPathEntry {
		
		private String name;
		
		private ContainerPathEntry parent;
		
		@SuppressWarnings("unused")
		private ContainerPathEntry child;
		
		public ContainerPathEntry(String name) {
			this.name = name;
		}
		
		private ContainerPathEntry addParent(String parentName) {
			ContainerPathEntry parentEntry = new ContainerPathEntry(parentName);
			this.parent = parentEntry;
			this.parent.child = this;
			return this.parent;
		}
		
		private String getPath(String pathSep) {
			
			String path = name;
			
			if (parent != null) {
				path = parent.getPath(pathSep) + pathSep + path;
			}
			
			return path;
		}
		
	}
	
	private String getContainerLabel(String containerQId, TemplateFacade template) {
		
		ContainerType containerDef = template.getContainerDefinition(containerQId);
		
		if (containerDef == null) {
			throw new IllegalArgumentException("Invalid container id [" + containerQId + "]");
		}
		
		return containerDef.getLabel();
	}

}
