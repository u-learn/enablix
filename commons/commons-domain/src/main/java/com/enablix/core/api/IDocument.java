package com.enablix.core.api;

import java.io.InputStream;

public interface IDocument {

	DocInfo getDocInfo();
	
	InputStream getDataStream();
	
}
