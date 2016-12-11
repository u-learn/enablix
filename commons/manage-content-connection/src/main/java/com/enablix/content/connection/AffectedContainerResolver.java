package com.enablix.content.connection;

import java.util.List;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.content.connection.ContentTypeConnection;

public interface AffectedContainerResolver {

	List<String> findAffectedContainers(ContentTemplate template, ContentTypeConnection contentConn);
	
}
