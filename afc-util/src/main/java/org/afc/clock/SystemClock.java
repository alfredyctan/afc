package org.afc.clock;

import java.util.Date;

/**
 * This implementation will return System clock directly for DateUtil.
 * 
 * You will need below bean definition in the Spring context file
 * <pre>
 * <bean id="Clock" class="org.afc.util.SystemClock" />
 * </pre>
 */
public class SystemClock implements Clock {

	@Override
	public Date getCurrentDate() {
		return new Date();
	}

	@Override
	public long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}
}
