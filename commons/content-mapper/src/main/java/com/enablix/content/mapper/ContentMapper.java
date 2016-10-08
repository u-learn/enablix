package com.enablix.content.mapper;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ContentMapper {

	EnablixContent transformToEnablixContent(ExternalContent content, ContentTemplate template);
	
	boolean isSupported(ContentSource source);
	
}
