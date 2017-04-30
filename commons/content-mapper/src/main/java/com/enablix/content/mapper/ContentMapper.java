package com.enablix.content.mapper;

import com.enablix.core.api.TemplateFacade;

public interface ContentMapper {

	EnablixContent transformToEnablixContent(ExternalContent content, TemplateFacade template);
	
	boolean isSupported(ContentSource source);
	
}
