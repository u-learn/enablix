package com.enablix.bayes;

public interface BayesService {
	
	void runBatch(ExecutionContext ctx, EBXNet net) throws Exception;
		
}