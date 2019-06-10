package org.afc.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

	private String namePrefix;

	private AtomicInteger i;

	private final ThreadGroup group;

	public NamedThreadFactory(String namePrefix) {
		this.namePrefix = namePrefix + '-';
		this.i = new AtomicInteger();
		group = (System.getSecurityManager() != null) ? System.getSecurityManager().getThreadGroup()
				: Thread.currentThread().getThreadGroup();
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(group, runnable, namePrefix + i.getAndIncrement(), 0);
		if (thread.isDaemon())
			thread.setDaemon(false);
		if (thread.getPriority() != Thread.NORM_PRIORITY)
			thread.setPriority(Thread.NORM_PRIORITY);
		return thread;
	}
}