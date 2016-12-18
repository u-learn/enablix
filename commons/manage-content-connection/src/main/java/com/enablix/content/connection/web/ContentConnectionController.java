package com.enablix.content.connection.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.service.CrudResponse;
import com.enablix.content.connection.ContentConnectionManager;
import com.enablix.core.domain.content.connection.ContentTypeConnection;

@RestController
@RequestMapping("contentconn")
public class ContentConnectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentConnectionController.class);
	
	@Autowired
	private ContentConnectionManager connMgr;
	
	@RequestMapping(method = RequestMethod.POST, value="/save", consumes = "application/json")
	public CrudResponse<ContentTypeConnection> saveConnection(
					@RequestBody ContentTypeConnection contentConn) {
		return connMgr.save(contentConn);
	}

	@RequestMapping(method = RequestMethod.GET, value="/r/{connectionIdentity}/", produces = "application/json")
	public ContentTypeConnection getContentConnection(@PathVariable String connectionIdentity) {
		LOGGER.debug("Fetch content connection record: {}", connectionIdentity);
		ContentTypeConnection content = connMgr.getContentConnection(connectionIdentity);
		return content;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/del/{connectionIdentity}/")
	public void deleteContentConnection(@PathVariable String connectionIdentity) {
		LOGGER.debug("Delete content connection record: {}", connectionIdentity);
		connMgr.deleteContentConnection(connectionIdentity);
	}
	
}