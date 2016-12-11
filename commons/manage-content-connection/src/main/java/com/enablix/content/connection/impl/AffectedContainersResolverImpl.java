package com.enablix.content.connection.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.QIdUtil;
import com.enablix.content.connection.AffectedContainerResolver;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.BoundedListFilter;
import com.enablix.services.util.template.walker.ContainerQIdCollector;
import com.enablix.services.util.template.walker.TemplateContainerWalker;

@Component
public class AffectedContainersResolverImpl implements AffectedContainerResolver {

	@Override
	public List<String> findAffectedContainers(ContentTemplate template, ContentTypeConnection contentConn) {
		
		List<String> affectedContainers = new ArrayList<>();
		
		String contentQId = contentConn.getContentQId();
		DataDefinitionType dataDefinition = template.getDataDefinition();
		
		ContainerType contentContainer = TemplateUtil.findContainer(dataDefinition, contentQId);
		
		if (contentContainer != null) {

			// content type is a container. Look for other containers where it is used as a reference data
			ContainerQIdCollector containerCollector = new ContainerQIdCollector();
			BoundedListFilter boundedRefListFilter = new BoundedListFilter(contentContainer);
			
			TemplateContainerWalker walker = new TemplateContainerWalker(template, boundedRefListFilter);
			walker.walk(containerCollector);
			
			affectedContainers = containerCollector.getContainerQIds();
					
		} else {
			
			// content type not a container. Check if it is an attribute of a container
			String parentQId = QIdUtil.getParentQId(contentQId);
			ContainerType parentContainer = TemplateUtil.findContainer(dataDefinition, parentQId);
			
			if (parentContainer != null) {
				affectedContainers.add(parentQId);
			}
			
		}
		
		return affectedContainers;
	}
	
}
