package com.enablix.app.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.DefaultContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRef;
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
			String containerInstanceIdentity) {
		NavigableContent navContent = createNavigableContent(templateId, containerQId, containerInstanceIdentity);
		return navContent == null ? "" : navContent.toPath("/");
	}

	private NavigableContent createNavigableContent(String templateId, String containerQId,
			String containerInstanceIdentity) {
		ContentDataRef dataRef = new ContentDataRef(templateId, containerQId, containerInstanceIdentity);
		NavigableContent navContent = navContentBuilder.build(dataRef, labelResolver);
		return navContent;
	}

	@Override
	public String resolveContentParentDataPath(String templateId, String containerQId, String containerInstanceIdentity) {
		
		NavigableContent navContent = createNavigableContent(templateId, containerQId, containerInstanceIdentity);
		
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

}
