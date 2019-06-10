package org.afc.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

public class LoggerNameFilter extends AbstractMatcherFilter<ILoggingEvent> {

    String loggerName;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (event.getLoggerName().equals(loggerName) || event.getLoggerName().startsWith(loggerName)) {
            return onMatch;
        } else {
            return onMismatch;
        }
    }

    public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

    public void start() {
        if (this.loggerName != null) {
            super.start();
        }
    }
}
