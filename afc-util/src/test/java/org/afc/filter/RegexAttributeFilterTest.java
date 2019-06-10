package org.afc.filter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.util.JUnit4Util;

public class RegexAttributeFilterTest {

	@Rule
	public TestName name = new TestName();
	
	private RegexAttributeFilter<Map<String, String>> filter;

	@Before
	public void setUp() throws Exception {
		JUnit4Util.startCurrentTest(getClass(), name);
	}
	
	@After
	public void tearDown() throws Exception {
		JUnit4Util.endCurrentTest(getClass(), name);
	}

	@Test
	public void testMatchExact() throws Exception {
		filter = new RegexAttributeFilter<>("STATUS", "LIVE", AttributeMock::getString);

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.actual(true);
		assertThat("String exatch match ", actual, is(equalTo(expect)));
	}

	@Test
	public void testMatchOr() throws Exception {
		filter = new RegexAttributeFilter<>("STATUS", "(LIVE|CLOSED)", AttributeMock::getString);

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.actual(true);
		assertThat("String or match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testMatchNotLiveOrClosed() throws Exception {
		filter = new RegexAttributeFilter<>("STATUS", "^((?!(LIVE|CLOSED)).)*$", AttributeMock::getString);

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.actual(false);
		assertThat("String not match", actual2, is(equalTo(expect2)));
	}
}
