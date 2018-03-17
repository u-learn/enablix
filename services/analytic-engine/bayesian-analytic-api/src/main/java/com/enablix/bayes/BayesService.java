package com.enablix.bayes;

public interface BayesService {
	
	void runBatch(ExecutionContext ctx, EBXNet net, RelevanceRecorder recorder) throws Exception;
		
}
