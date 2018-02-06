package com.enablix.bayes;

import org.junit.Test;

import com.enablix.bayes.EBXNet.EBXBoolNode;

public class NeticaIntegrationTest {
	
/**
 * Generated from EBXNode#toString()
 * 	probs = 
		// true         false         // IS_REDUNDANT IS_NEW IS_RELATED_TO_BIZ_ROLE 
		  (0.3,         0.7,          // true         true   true                   
		   0.7,         0.3,          // true         true   false                  
		   0.99,        0.01,         // true         false  true                   
		   0.9,         0.1,          // true         false  false                  
		   0.05,        0.95,         // false        true   true                   
		   0.05,        0.95,         // false        true   false                  
		   0.3,         0.7,          // false        false  true                   
		   0.3,         0.7);         // false        false  false  
 */


	
	//Can we make a better API?
	static final float[] probs = {
		.3F,.7F,
		.7F,.3F,
		.99F,.01F,
		.90F,.10F,
		.05F,.95F,
		.05F,.95F,
		.30F,.70F,
		.3F,.7F
	};
	
	@Test
	public void test() throws Exception {
		EBXNet net = new EBXNet();
		//Target
		EBXBoolNode isInteresting = net.createBoolNode("IS_INTERESTING", "Is this content interesting?");
		
		//Priors
		EBXBoolNode isNew = net.createBoolNode("IS_NEW", "Is this content new?").setPriors(.5f);
		EBXBoolNode isRedundant = net.createBoolNode("IS_REDUNDANT","Is this redundant content?").setPriors(.8f);
		EBXBoolNode isRelatedToBizRole = net.createBoolNode("IS_RELATED_TO_BIZ_ROLE", "Is this content in the users business role?").setPriors(.3f);
		
		isInteresting.linkFrom(isRedundant);
		isInteresting.linkFrom(isNew);
		isInteresting.linkFrom(isRelatedToBizRole);
		
		net.target(isInteresting);
		
		isInteresting.setCPTable(probs);
		net.compile();
		float exp1 = net.getProbability();
		
		isRedundant.getNode().finding().enterState("false");
		isNew.getNode().finding().enterState("true");
		isRelatedToBizRole.getNode().finding().enterState("true");
		
		float exp3 = net.getProbability();
		
		//Whole net to string
		System.out.println(net);
		
		net.getNet().retractFindings();
		
		//Testing re-flow
		isNew.setPriors(.2f);
		isRedundant.setPriors(.2f);
		float exp2 = net.getProbability();
		if(exp1 == exp2){
			throw new Exception(String.format("Expected experiment 1 (%f) to be a different value than experiment 2 (%f)",exp1,exp2));
		}
		System.out.println(String.format("Experiment 1 (%f) vs Experiment 2 (%f)",exp1,exp2));
		System.out.println(String.format("After enter state 1 (%f)",exp3));
		
		//!IMPORTANT!
		//Testing toString methods
		
		//Whole net to string
		System.out.println(net);
		
		//Node to string
		System.out.println(isInteresting);
		
	}
}
