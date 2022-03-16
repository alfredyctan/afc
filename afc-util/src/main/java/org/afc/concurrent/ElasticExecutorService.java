package org.afc.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.afc.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticExecutorService extends ThreadPoolExecutor implements RejectedExecutionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ElasticExecutorService.class);

	public ElasticExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, String namePrefix) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new SynchronousQueue<>(), new NamedThreadFactory(namePrefix));
		logger.info("corePoolSize:[{}], maximumPoolSize:[{}], keepAliveTime:[{} {}]", corePoolSize, maximumPoolSize, keepAliveTime, unit);
		setRejectedExecutionHandler(this);
	}

	private static class Queued implements Runnable {

		private Runnable runnable;

		private long start;

		public Queued(Runnable runnable) {
			this.runnable = runnable;
			this.start = ClockUtil.currentTimeMillis();
		}

		@Override
		public void run() {
			long end = ClockUtil.currentTimeMillis();
			logger.info("task queued for [{}] ms", (end - start));
			runnable.run();
		}
	}

	@Override
	public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
		if (!executor.isShutdown()) {
			try {
				logger.info("attempting to queue task execution");
				executor.getQueue().put(new Queued(runnable));
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
				throw new RejectedExecutionException("executor is interrupted");
			}
		} else {
			throw new RejectedExecutionException("executor has been shut down");
		}
	}
}