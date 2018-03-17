package com.enablix.bayes;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.domain.content.UserContentRelevance;

public interface RelevanceRecorder {

	void write(List<UserContentRelevance> recUserContentRelevance, ContentDataRecord dataRec) throws Exception;
	
	void close();

	void open(ExecutionContext ctx);
}
