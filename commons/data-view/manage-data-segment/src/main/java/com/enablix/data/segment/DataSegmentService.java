package com.enablix.data.segment;

import com.enablix.core.domain.segment.DataSegment;
import com.enablix.data.view.DataView;

public interface DataSegmentService {

	DataView getDataViewForUserId(String userId);
	
	DataView getDataViewForUserProfileIdentity(String userProfileIdentity);
	
	DataView buildDataView(DataSegment ds);

	void save(DataSegment ds);
	
}
