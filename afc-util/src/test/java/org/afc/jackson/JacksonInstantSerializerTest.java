package org.afc.jackson;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.util.ClockUtil;
import org.afc.util.JUnit4Util;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonInstantSerializerTest {

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
	public void test() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		ClockUtil.fixedClock("2007-12-03T10:15:30.00Z");
		
		MockObject mock = MockObject.builder()
			.event("new")
			.context("{\"id\":123,\"status\":\"PENDING\",\"detail\":{}}")
			.message("short msg")
			.organization("organization")
			.audience("username")
			.instant(ClockUtil.instant())
			.build();
		
		StringWriter writer = new StringWriter();
		objectMapper.writeValue(writer, mock);
		
		String actual = JUnit4Util.actual(writer.toString());
		String expect = JUnit4Util.expect("{\"event\":\"new\"," + 
			"\"context\":{\"id\":123,\"status\":\"PENDING\",\"detail\":{}}," + 
			"\"message\":\"short msg\",\"organization\":\"organization\"," + 
			"\"audience\":\"username\",\"instant\":\"2007-12-03T10:15:30Z\"}"
		);
		assertThat("", actual, is(equalTo(expect)));
	}
}
