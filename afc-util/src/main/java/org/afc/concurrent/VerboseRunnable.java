package org.afc.concurrent;

import org.slf4j.LoggerFactory;

public class VerboseRunnable implements Runnable {

	private Runnable runnable;

	public VerboseRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		try {
			runnable.run();
		} catch (Throwable t) {
			LoggerFactory.getLogger(runnable.getClass()).error("uncaught throwable", t);
		}
	}
}