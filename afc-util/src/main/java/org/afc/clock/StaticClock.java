package org.afc.clock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation will fix clock get by DateUtil.
 * 
 * You will need below bean definition in the Spring context file
 * <pre>
 * <bean id="Clock" class="org.afc.util.StaticClock">
 *    <property name="staticDate" value="2010-03-16 11:14:52.347"/>
 *    <property name="freezeTime" value="true"/>
 * </bean> 
 * </pre>
 */
public class StaticClock implements Clock {

	private static Logger logger = LoggerFactory.getLogger(Clock.class);

	private long staticTime;

	private long startTime;

	private boolean freezeTime;

	public void setStaticDate(String staticDate) {
		this.startTime = System.currentTimeMillis();
		try {
			staticTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(staticDate).getTime();
		} catch (ParseException e) {
			logger.error(e.toString());
		}
	}

	public void setFreezeTime(boolean freezeTime) {
    	this.freezeTime = freezeTime;
    }

	@Override
	public Date getCurrentDate() {
		return new Date(getCurrentTimeMillis());
	}

	@Override
	public long getCurrentTimeMillis() {
		return (freezeTime)?
				staticTime:
				staticTime + (System.currentTimeMillis() - this.startTime);
	}
}
