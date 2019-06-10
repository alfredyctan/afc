package org.afc.eventbus;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.util.JUnit4Util;
import org.afc.util.JUnitUtil;

public class GuavaIntraProcessEventBusTest {

	@Rule
	public TestName name = new TestName();

	@Before
	public void setUp() throws Exception {
		JUnit4Util.startCurrentTest(getClass(), name);
	}
	
	@After
	public void tearDown() throws Exception {
		JUnit4Util.endCurrentTest(getClass(), name);
	}

	
	@Test
	public void test() {
		GuavaIntraProcessEventBus bus = new GuavaIntraProcessEventBus();
		MockListener mock1 = new MockListener();
		MockListener mock2 = new MockListener();
		
		bus.register("common", mock1);
		bus.register("common", mock2);
		
		bus.post("common", "hello");
		
		String actual1 = JUnitUtil.actual(mock1.received.get(0));
		String actual2 = JUnitUtil.actual(mock2.received.get(0));
		String expect1 = JUnitUtil.actual("hello");
		String expect2 = JUnitUtil.actual("hello");
		
		assertThat("listener 1", actual1, is(equalTo(expect1)));
		assertThat("listener 2", actual2, is(equalTo(expect2)));
	}
}
