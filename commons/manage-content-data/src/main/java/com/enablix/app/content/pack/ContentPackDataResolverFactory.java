package com.enablix.app.content.pack;

import com.enablix.core.domain.content.pack.ContentPack;

public interface ContentPackDataResolverFactory {

	@SuppressWarnings("rawtypes")
	ContentPackDataResolver getResolver(ContentPack.Type packType);
	
}
