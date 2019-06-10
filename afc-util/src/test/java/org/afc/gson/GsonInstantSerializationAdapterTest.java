package org.afc.gson;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.util.ClockUtil;
import org.afc.util.JUnit4Util;
import com.google.gson.Gson;

public class GsonInstantSerializationAdapterTest {

	@Rule
	public TestName name = new TestName();

	@BeforeClass
	public static void setUpBeforeClass() {
		ClockUtil.fixedClock("2007-12-03T10:15:30.00Z");
	}
	
	@Before
	public void setUp() throws Exception {
		JUnit4Util.startCurrentTest(getClass(), name);
	}
	
	@After
	public void tearDown() throws Exception {
		JUnit4Util.endCurrentTest(getClass(), name);
	}
	
	@Test
	public void testDeserialize() {
		String payload = new String("{\"instant\":\"2007-12-03T10:15:30Z\"}");

		MockObject actual = JUnit4Util.actual(new Gson().fromJson(payload, MockObject.class));
		MockObject expect = JUnit4Util.expect(MockObject.builder()
			.instant(ClockUtil.instant())
			.build()
		);
		assertThat("", actual, is(equalTo(expect)));
	}

	@Test
	public void testSerialize() throws Exception {
		MockObject mock = MockObject.builder()
			.instant(ClockUtil.instant())
			.build();
		
		String actual = JUnit4Util.actual(new Gson().toJson(mock));
		String expect = JUnit4Util.expect("{\"instant\":\"2007-12-03T10:15:30Z\"}");
		assertThat("", actual, is(equalTo(expect)));
	}
}
