package org.afc.logging;

import static org.afc.util.CollectionUtil.*;
import static org.afc.util.OptionalUtil.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class TestAppender extends AppenderBase<ILoggingEvent> {

	public static Map<String, List<ILoggingEvent>> events = new ConcurrentHashMap<>();

    @Override
    protected void append(ILoggingEvent e) {
    	List<ILoggingEvent> list = ensureConcurrently(events, e.getLoggerName(), ArrayList::new);
    	list.add(e);
    }

    public static void clear() {
    	events.values().forEach(List::clear);
    }

    public static void clear(Class<?> logger) {
    	clear(logger.getName());
    }

    public static void clear(String loggerName) {
    	ifNotNull(events.get(loggerName), List::clear);
    }

    public static List<String> messages(Class<?> logger) {
    	return messages(logger.getName());
    }

    public static List<String> messages(String loggerName) {
    	return events.get(loggerName).stream()
    		.map(ILoggingEvent::getFormattedMessage)
    		.collect(toList());
    }
}