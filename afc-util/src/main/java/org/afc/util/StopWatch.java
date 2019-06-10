package org.afc.util;

import java.util.Arrays;

public class StopWatch {

	private static final long NANO2MILLI = 1000000;

	private long ticks[];

	private String pattern;

	private int current;

	/**
	 * default stop watch with max 1024 tick
	 */
	public StopWatch() {
		this(1024, null);
	}

	/**
	 * stop watch with given max tick
	 * 
	 * @param tick - the maximum number of tick
	 */
	public StopWatch(int tick) {
		this(tick, null);
	}

	/**
	 * stop watch with given max tick and toString pattern
	 * 
	 * @param tick - the maximum number of tick
	 * @param pattern - String.format() pattern for toString of split time
	 */
	public StopWatch(int tick, String pattern) {
		this.ticks = new long[tick];
		this.pattern = pattern;
		this.current = 0;
	}
	
	/**
	 * tick the stop watich 
	 */
	public void tick() {
		ticks[current++] = System.nanoTime();
	}

	/**
	 * Get the split in nano seconds
	 * 
	 * @param split - the split number between tick, always less than tick - 1
	 * @return
	 */
	public long nanos(int split) {
		return ticks[split + 1] - ticks[split];
	}

	/**
	 * Get the split in milli seconds, with double precision 
	 * 
	 * @param split
	 * @return
	 */
	public double millis(int split) {
		return ((double) (ticks[split + 1] - ticks[split]) / NANO2MILLI);
	}

	@Override
	public String toString() {
		long splits[] = new long[ticks.length - 1];
		for (int i = 0; i < splits.length; i++) {
			splits[i] = ticks[i + 1] - ticks[i];
		}

		return (pattern != null) ? String.format(pattern, splits) : Arrays.toString(splits);
	}
}
