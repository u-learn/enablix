package com.enablix.play.definition.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentSetType;
import com.enablix.play.exec.PlayExecutor;

@RestController
@RequestMapping("play/data")
public class PlayDefinitionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlayDefinitionController.class);
	
	@Autowired
	private PlayExecutor playExecutor;
	
	@RequestMapping(method = RequestMethod.POST, value="/contentsetrecords", produces = "application/json")
	public List<ContentDataRecord> getContentSetRecords(@RequestBody ContentSetType contentSet) {
		LOGGER.debug("Fetching content set records");
		return playExecutor.findContentSetRecords(contentSet);
	}
	
}