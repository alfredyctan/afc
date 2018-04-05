package org.afc.util;

import java.util.Date;

import org.afc.clock.Clock;
import org.afc.clock.SystemClock;

/**
 * 
 * DateUtil library to provide static date function for regression testing
 * 
 * You will need bean definition in the spring context file
 * for System Clock
 * <pre>
 * <bean id="DateUtil" class="org.afc.util.SystemClock" />
 * </pre> 
 *
 * for static clock
 * <pre>
 * <bean id="DateUtil" class="org.afc.util.StaticClock">
 *    <property name="staticDate" value="2010-03-16 11:14:52.347"/>
 *    <property name="freezeTime" value="true"/>
 * </bean> 
 * </pre>
 */
public class ClockUtil {

	private static Clock instance;

	static {
		instance = new SystemClock();
	}

	public static Clock getInstance() {
		return instance;
	}

	public static void setInstance(Clock instance) {
		ClockUtil.instance = instance;
	}

	public static Date currentDate() {
		return instance.getCurrentDate();
	}

	public static long currentTimeMillis() {
		return instance.getCurrentTimeMillis();
	}
}
