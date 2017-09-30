package com.enablix.analytics.info.detection;

import java.util.Collection;
import java.util.List;

public interface TaggedInfo {

	List<InfoTag> tags();

	void addTags(Collection<InfoTag> infoTags);
	
}
