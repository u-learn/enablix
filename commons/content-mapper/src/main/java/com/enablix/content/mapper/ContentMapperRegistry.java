package com.enablix.content.mapper;

public interface ContentMapperRegistry {

	ContentMapper getMapper(ExternalContent content);
	
}
