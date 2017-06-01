package com.enablix.content.connection;

import java.util.List;

import com.enablix.app.service.CrudResponse;
import com.enablix.content.connection.web.ContentTypeLinkVO;
import com.enablix.core.domain.content.connection.ContentTypeConnection;

public interface ContentConnectionManager {

	CrudResponse<ContentTypeConnection> save(ContentTypeConnection contentConnection);
	
	ContentTypeConnection getContentConnection(String connectionIdentity);

	void refreshHoldingContainersForAllConnections();

	void deleteContentConnection(String connectionIdentity);

	List<ContentTypeLinkVO> getContentTypeLinkVO(String contentQId);
	
}
