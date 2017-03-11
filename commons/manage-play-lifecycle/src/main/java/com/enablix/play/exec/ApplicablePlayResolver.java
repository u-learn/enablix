package com.enablix.play.exec;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.play.PlayDefinition;

public interface ApplicablePlayResolver {

	List<PlayDefinition> findFocusedPlays(ContentDataRecord focusRecord);
	
}
