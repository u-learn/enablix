package com.enablix.app.content.bulkimport;

import com.enablix.commons.exception.ValidationException;
import com.enablix.core.domain.content.ImportRequest;

public interface ImportManager {

	void acceptRequest(ImportRequest request) throws ValidationException;
	
}
