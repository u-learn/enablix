package com.enablix.core.domain.config;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ebx_configuration")
public class Configuration extends Settings {

	public Configuration() {
		super();
	}
	
}
