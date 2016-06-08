package com.enablix.commons.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {

	private static final String DEFAULT_THREAD_NAME_PREFIX = "-thread-";
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String threadNamePrefix;

	public DefaultThreadFactory(String poolName) {
		this(poolName, DEFAULT_THREAD_NAME_PREFIX);
	}

	public DefaultThreadFactory(String poolName, String threadPrefix) {
		final SecurityManager s = System.getSecurityManager();
		group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		threadNamePrefix = poolName + threadPrefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		final Thread t = new Thread(group, r, threadNamePrefix + threadNumber.getAndIncrement(), 0);
		if (t.isDaemon()) {
			t.setDaemon(false);
		}
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}

}
