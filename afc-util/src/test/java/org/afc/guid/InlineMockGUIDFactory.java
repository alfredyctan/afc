package org.afc.guid;

import java.util.Arrays;
import java.util.List;

public class InlineMockGUIDFactory<T> implements GUIDFactory<T> {

	private List<T> ids;
	
	private int p;

	public InlineMockGUIDFactory(T... ids) {
		reset(ids);
	}
	
	@Override
	public T generate() {
		p = (p + 1) % ids.size();
		return ids.get(p);
	}

	public void reset(T... ids) {
		this.ids = Arrays.asList(ids);
		this.p = -1;
	}
}
