package com.enablix.commons.util.concurrent;

import java.util.concurrent.ExecutorService;

public class Executors {

	public static ExecutorService newFixedThreadPool(String threadPoolName, int threadCount) {
		return java.util.concurrent.Executors.newFixedThreadPool(threadCount, new DefaultThreadFactory(threadPoolName));
	}
	
}
