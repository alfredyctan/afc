package org.afc.jackson;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

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

public class JacksonInstantDeserializerTest {

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
	public void testJackson() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		byte[] payload = new String(
			"{\"event\":\"new\",\n\"context\":{ \"id\": 123, \n \"status\": \"PENDING\", \"detail\": {} },"+
			"\"message\":\"short msg\",\"organization\":\"organization\",\"audience\":\"username\",\"instant\":\"2007-12-03T10:15:30Z\"}"
		).getBytes();
		
		ClockUtil.fixedClock("2007-12-03T10:15:30.00Z");
		try {
			MockObject actual = JUnit4Util.actual(objectMapper.readValue(payload, objectMapper.constructType(MockObject.class)));
			MockObject expect = JUnit4Util.expect(MockObject.builder()
				.event("new")
				.context("{\"id\":123,\"status\":\"PENDING\",\"detail\":{}}")
				.message("short msg")
				.organization("organization")
				.audience("username")
				.instant(ClockUtil.instant())
				.build()
			);
			assertThat("", actual, is(equalTo(expect)));
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
