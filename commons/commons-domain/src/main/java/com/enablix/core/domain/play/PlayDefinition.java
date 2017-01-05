package com.enablix.core.domain.play;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.PlayTemplate;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_play_definition")
public class PlayDefinition extends BaseDocumentEntity {
	
	private PlayTemplate playTemplate;

	public PlayTemplate getPlayTemplate() {
		return playTemplate;
	}

	public void setPlayTemplate(PlayTemplate playTemplate) {
		this.playTemplate = playTemplate;
	}
	
}
