package com.enablix.app.content.pack;

import com.enablix.core.api.ContentPackDTO;
import com.enablix.data.view.DataView;

public interface ContentPackManager {

	ContentPackDTO getContentPack(String packIdentity, DataView dataView, int pageNo, int pageSize);
	
}
