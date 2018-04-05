package org.afc.logging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.afc.util.StringUtil;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ThreadIdConverter extends ClassicConverter {

	private Map<String, String> threadIds;
	
	public ThreadIdConverter() {
		this.threadIds = new ConcurrentHashMap<>();
	}
	
	public String convert(ILoggingEvent event) {
		String name = event.getThreadName();
		String id = threadIds.get(name);  
		if (id == null) {
			Thread thread = getThreadByName(name);
			id = (thread != null) ? StringUtil.fixLengthInsert('0', String.valueOf(thread.getId()), 5) : name;
			threadIds.put(name, id);
		}
		return id;
	}

	private static Thread getThreadByName(String threadName) {
	    for (Thread thread : Thread.getAllStackTraces().keySet()) {
	        if (thread.getName().equals(threadName)) {
	        	return thread;
	        }
	    }
	    return null;
	}
}
