package com.enablix.app.content.pack;

import org.springframework.data.domain.Page;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.content.pack.ContentPack;
import com.enablix.data.view.DataView;

public interface ContentPackDataResolver<T extends ContentPack> {

	Page<ContentDataRecord> getData(T pack, DataView dataView, int pageNo, int pageSize);
	
	ContentPack.Type resolverPackType();
	
}
