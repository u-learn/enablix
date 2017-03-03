package com.enablix.play.definition.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.commons.xsdtopojo.PlayTemplate;
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
	
	@RequestMapping(method = RequestMethod.POST, value="/update", produces = "application/json")
	public void updatePlayDefinition(@RequestBody PlayTemplate playTemplate) {
		LOGGER.debug("Updating play definition");
		playDefMgr.savePlayTemplate(playTemplate);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/active/update", produces = "application/json")
	public void updateActiveStatus(@RequestBody ActivateRequest request) {
		LOGGER.debug("Updating play definition status");
		playDefMgr.updateActiveStatus(request.getPlayDefId(), request.isActive());
	}
	
	static class ActivateRequest {
		
		private String playDefId;
		private boolean active;
		
		public String getPlayDefId() {
			return playDefId;
		}
		
		public void setPlayDefId(String playDefId) {
			this.playDefId = playDefId;
		}
		
		public boolean isActive() {
			return active;
		}
		
		public void setActive(boolean active) {
			this.active = active;
		}
		
	}
	
}