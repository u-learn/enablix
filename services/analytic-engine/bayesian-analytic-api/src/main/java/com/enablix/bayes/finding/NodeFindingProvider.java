package com.enablix.bayes.finding;

import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.InitializationContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.security.authorization.UserProfile;

public interface NodeFindingProvider {

	String getFinding(ExecutionContext ctx, UserProfile user, ContentDataRecord dataRec);
	
	String nodeName();
	
	void init(InitializationContext ctx);

	
	
}
