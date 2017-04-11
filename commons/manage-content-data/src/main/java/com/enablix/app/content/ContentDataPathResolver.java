package com.enablix.app.content;


public interface ContentDataPathResolver {

	String addContainerLabelToPath(String templateId, String containerQId, String currentPath);

	String resolveContentParentDataPath(String templateId, String containerQId, String containerInstanceIdentity,
			String containerInstanceTitle);

	String resolveContentDataPath(String templateId, String containerQId, String containerInstanceIdentity,
			String containerInstanceTitle);
	
	String resolveContainerPath(String templateId, String containerQId);

	String appendPath(String currentPath, String addPath);
	
}
