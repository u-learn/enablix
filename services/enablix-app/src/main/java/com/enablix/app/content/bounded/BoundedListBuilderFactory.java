package com.enablix.app.content.bounded;

import com.enablix.core.commons.xsdtopojo.BoundedType;

public interface BoundedListBuilderFactory {

	BoundedListBuilder getBuilder(BoundedType boundedType);
	
}
