package com.enablix.content.connection;

import com.enablix.app.service.CrudResponse;
import com.enablix.core.domain.content.connection.ContentTypeConnection;

public interface ContentConnectionManager {

	CrudResponse<ContentTypeConnection> save(ContentTypeConnection contentConnection);
	
	ContentTypeConnection getContentConnection(String connectionIdentity);

	void refreshHoldingContainers(ContentTypeConnection contentConnection);

	void refreshHoldingContainersForAllConnections();
	
}
