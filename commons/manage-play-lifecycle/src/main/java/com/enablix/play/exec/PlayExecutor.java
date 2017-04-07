package com.enablix.play.exec;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentSetType;
import com.enablix.data.view.DataView;

public interface PlayExecutor {

	List<ContentDataRecord> findContentSetRecords(ContentSetType contentSet, DataView userView);
	
}
