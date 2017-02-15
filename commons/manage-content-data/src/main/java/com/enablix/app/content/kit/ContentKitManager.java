package com.enablix.app.content.kit;

import com.enablix.app.service.CrudResponse;
import com.enablix.core.domain.content.kit.ContentKit;

public interface ContentKitManager {

	CrudResponse<ContentKit> saveOrUpdateKit(ContentKit kit);
	
	ContentKitBundle getContentKitBundle(String kitIdentity);

	void deleteKit(String contentKitIdentity);

	ContentKit getContentKit(String contentKitIdentity);
	
	ContentKitDetail getContentKitDetail(String contentKitIdentity);
	
}
