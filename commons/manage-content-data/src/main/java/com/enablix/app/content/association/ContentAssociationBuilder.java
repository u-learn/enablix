package com.enablix.app.content.association;

import java.util.Collection;
import java.util.Map;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.core.domain.content.ContentAssociation;
import com.enablix.services.util.template.TemplateWrapper;

public interface ContentAssociationBuilder {

	Collection<ContentAssociation> buildAssociations(TemplateWrapper template, ContentUpdateContext updateCtx,
			Map<String, Object> dataAsMap);
	
	boolean replaceExisting();
	
}
