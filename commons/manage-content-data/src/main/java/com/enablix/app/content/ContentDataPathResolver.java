package com.enablix.app.content;


public interface ContentDataPathResolver {

	String resolveContentDataPath(String templateId, String containerQId, String containerInstanceIdentity);
	
	String resolveContentParentDataPath(String templateId, String containerQId, String containerInstanceIdentity);
	
	String addContainerLabelToPath(String templateId, String containerQId, String currentPath);
	
}
