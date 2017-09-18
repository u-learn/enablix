package com.enablix.services.util.template.walker;

import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public class TemplateContentItemWalker {

	private ContentTemplate template;
	
	public TemplateContentItemWalker(ContentTemplate template) {
		this.template = template;
	}
	
	public void walk(ContentItemVisitor visitor) {
		TemplateContainerWalker containerWalker = new TemplateContainerWalker(template);
		containerWalker.walk((container) -> {
			for (ContentItemType item : container.getContentItem()) {
				visitor.visit(container, item);
			}
		});
	}
	
}
