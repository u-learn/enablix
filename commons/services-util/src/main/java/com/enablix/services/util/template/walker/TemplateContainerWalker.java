package com.enablix.services.util.template.walker;

import java.util.List;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public class TemplateContainerWalker {

	private ContentTemplate template;
	private ContainerFilter filter;
	
	public TemplateContainerWalker(ContentTemplate template) {
		this(template, new VisitAllContainerFilter());
	}
	
	public TemplateContainerWalker(ContentTemplate template, ContainerFilter filter) {
		this.template = template;
		this.filter = filter;
	}
	
	public void walk(ContainerVisitor visitor) {
		walkAndProcess(template.getDataDefinition().getContainer(), visitor);
	}
	
	protected void walkAndProcess(List<ContainerType> containers, ContainerVisitor visitor) {
		
		for (ContainerType container : containers) {
			
			if (filter.accept(container)) {
				visitor.visit(container);
			}
			
			walkAndProcess(container.getContainer(), visitor);
		}
	}
	
}
