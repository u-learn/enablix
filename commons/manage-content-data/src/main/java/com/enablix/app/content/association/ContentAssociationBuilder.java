package com.enablix.app.content.association;

import java.util.Collection;
import java.util.Map;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.content.ContentAssociation;

public interface ContentAssociationBuilder {

	Collection<ContentAssociation> buildAssociations(TemplateFacade template, ContentUpdateContext updateCtx,
			Map<String, Object> dataAsMap);
	
	boolean replaceExisting();
	
}
