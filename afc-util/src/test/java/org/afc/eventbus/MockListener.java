package org.afc.eventbus;

import java.util.LinkedList;
import java.util.List;

import com.google.common.eventbus.Subscribe;

public class MockListener {
	
	List<String> received = new LinkedList<>();
	
	@Subscribe
	public void accept(String event) {
		received.add(event);
	}
}
