package com.enablix.app.content.bulkimport;

public interface ImportProcessorFactory {

	ImportProcessor getProcessor(String source);
	
}
