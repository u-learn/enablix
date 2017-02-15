package com.enablix.app.content.kit.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.kit.ContentKitBundle;
import com.enablix.app.content.kit.ContentKitManager;
import com.enablix.app.content.kit.ContentKitDetail;
import com.enablix.app.service.CrudResponse;
import com.enablix.core.domain.content.kit.ContentKit;

@RestController
@RequestMapping("contentkit")
public class ContentKitController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentKitController.class);
	
	@Autowired
	private ContentKitManager ckMgr;
	
	@RequestMapping(method = RequestMethod.POST, value="/save", consumes = "application/json")
	public CrudResponse<ContentKit> saveConnection(@RequestBody ContentKit contentConn) {
		return ckMgr.saveOrUpdateKit(contentConn);
	}

	@RequestMapping(method = RequestMethod.GET, value="/r/{contentKitIdentity}/", produces = "application/json")
	public ContentKit getContentKit(@PathVariable String contentKitIdentity) {
		LOGGER.debug("Fetch content kit: {}", contentKitIdentity);
		ContentKit contentKit = ckMgr.getContentKit(contentKitIdentity);
		return contentKit;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/b/{contentKitIdentity}/", produces = "application/json")
	public ContentKitBundle getContentKitBundle(@PathVariable String contentKitIdentity) {
		LOGGER.debug("Fetch content kit bundle: {}", contentKitIdentity);
		ContentKitBundle bundle = ckMgr.getContentKitBundle(contentKitIdentity);
		return bundle;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/d/{contentKitIdentity}/", produces = "application/json")
	public ContentKitDetail getLinkedKitDetail(@PathVariable String contentKitIdentity) {
		LOGGER.debug("Fetch content kit - linked kit summary: {}", contentKitIdentity);
		ContentKitDetail summary = ckMgr.getContentKitDetail(contentKitIdentity);
		return summary;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/del/{contentKitIdentity}/")
	public void deleteContentKit(@PathVariable String contentKitIdentity) {
		LOGGER.debug("Delete content kit record: {}", contentKitIdentity);
		ckMgr.deleteKit(contentKitIdentity);
	}
	
}