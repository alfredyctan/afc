package org.afc.guid;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongMockGUIDFactory implements GUIDFactory<Long> {

	private AtomicLong id;
	
	public AtomicLongMockGUIDFactory() {
		reset(); 
	}

	@Override
	public Long generate() {
		return id.getAndIncrement();
	}

	public void reset() {
		this.id = new AtomicLong(1L); 
	}

	public void reset(long start) {
		this.id = new AtomicLong(start); 
	}
}
