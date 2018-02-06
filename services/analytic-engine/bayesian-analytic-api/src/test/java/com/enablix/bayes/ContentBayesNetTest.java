package com.enablix.bayes;

import org.junit.Test;

import com.enablix.bayes.content.ContentBayesNet;

public class ContentBayesNetTest {

	@Test
	public void test() throws Exception {
		
		EBXNet net = ContentBayesNet.build();
		net.compile();
		
		float relevancy = net.getProbability();
		
		System.out.println(String.format("Relevancy - (%f)", relevancy));
		
		System.out.println(net);
		
		System.out.println(net.target());
		
	}
	
}
