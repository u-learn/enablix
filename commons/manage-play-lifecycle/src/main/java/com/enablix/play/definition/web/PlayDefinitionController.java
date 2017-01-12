package com.enablix.play.definition.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.play.PlayDefinition;
import com.enablix.play.definition.PlayDefinitionManager;

@RestController
@RequestMapping("play/def")
public class PlayDefinitionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlayDefinitionController.class);
	
	@Autowired
	private PlayDefinitionManager playDefMgr;
	
	@RequestMapping(method = RequestMethod.GET, value="/r/{playDefId}", produces = "application/json")
	public PlayDefinition getPlayDefinition(@PathVariable String playDefId) {
		LOGGER.debug("Getting play definition for id: {}", playDefId);
		return playDefMgr.getPlayDefinition(playDefId);
	}
	
}