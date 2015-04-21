package com.enablix.commons.dir.watch;

import java.io.FilenameFilter;

public interface DirectoryWatcher {

	void setFilenameFilter(FilenameFilter filter);
	
	void setFileCreateOrUpdateCallback(FileCreateOrUpdateCallback callback);
	
	void start();
	
}
