package com.enablix.commons.dir.watch;

import java.io.File;

public interface FileCreateOrUpdateCallback {

	void onFileCreated(File fileName);
	
	void onFileUpdated(File fileName);
	
}
