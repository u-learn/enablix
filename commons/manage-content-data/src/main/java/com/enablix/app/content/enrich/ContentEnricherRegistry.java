package com.enablix.app.content.enrich;

import java.util.Collection;

public interface ContentEnricherRegistry {

	Collection<ContentEnricher> getEnrichers();
	
}
