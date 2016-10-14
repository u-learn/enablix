package com.enablix.core.domain.activity;

/*
 * @Deprecated
 * Use DocumentActivity instead
 */
public class DocDownload extends DocumentActivity {

	protected DocDownload() { 
		// for ORM
	}
	
	public DocDownload(String itemIdentity, String containerQId, 
			ContainerType containerType, String docIdentity, String docTitle) {
		this(itemIdentity, containerQId, containerType, docIdentity, null, null, null, docTitle);
	}

	public DocDownload(String itemIdentity, String containerQId, ContainerType containerType, 
			String docIdentity, String contextName, String contextId, String contextTerm, String contentTitle) {
		
		super(ContentActivityType.DOC_DOWNLOAD, itemIdentity, containerQId, 
				containerType, docIdentity, contextName, contextId, contextTerm, contentTitle);
	}

	
}
