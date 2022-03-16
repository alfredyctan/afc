package org.afc.concurrent;

import org.afc.logging.SDC;
import org.slf4j.LoggerFactory;

public class VerboseRunnable implements Runnable {

	private String sdc;

	private Runnable runnable;

	public VerboseRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	public VerboseRunnable(String sdc, Runnable runnable) {
		this.sdc = sdc;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		try {
			SDC.set(sdc);
			runnable.run();
		} catch (Exception e) {
			LoggerFactory.getLogger(runnable.getClass()).error("uncaught exception", e);
		}
	}
}