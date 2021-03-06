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

public class LesserAttributeFilterTest {

	@Rule
	public TestName name = new TestName();
	
	private LesserAttributeFilter<Map<String, String>> filter;

	@Before
	public void setUp() throws Exception {
		JUnit4Util.startCurrentTest(getClass(), name);
	}
	
	@After
	public void tearDown() throws Exception {
		JUnit4Util.endCurrentTest(getClass(), name);
	}
	
	@Test
	public void testString() throws Exception {
		filter = new LesserAttributeFilter<>("STATUS", "FULL", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("String equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(true);
		assertThat("String equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testNumeric() throws Exception {
		filter = new LesserAttributeFilter<>("AMOUNT", "700000", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("Numeric equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(true);
		assertThat("Numeric equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testLocalDate() throws Exception {
		filter = new LesserAttributeFilter<>("LOCALDATE", "2018-06-20", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(true);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testLocalTime() throws Exception {
		filter = new LesserAttributeFilter<>("LOCALTIME", "09:15:30", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(true);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testLocalDateTime() throws Exception {
		filter = new LesserAttributeFilter<>("LOCALDATETIME", "2018-06-20T09:15:30", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(true);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testOffsetDateTimer() throws Exception {
		filter = new LesserAttributeFilter<>("OFFSETDATETIME", "2018-06-20T09:15:30+08:00", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(true);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testZonedDateTime() throws Exception {
		filter = new LesserAttributeFilter<>("ZONEDDATETIME", "2018-06-20T09:15:30+08:00[Asia/Hong_Kong]", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(true);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}
	

	@Test
	public void testAttributeNotFound() throws Exception {
		filter = new LesserAttributeFilter<>("notexist", "?", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("attributeNotFound", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(false);
		assertThat("attributeNotFound", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testDateTypeMismatch() throws Exception {
		filter = new LesserAttributeFilter<>("AMOUNT", "2018-06-20", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("String attributeNotFound", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(false);
		assertThat("String attributeNotFound", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testNumericTypeMismatch() throws Exception {
		filter = new LesserAttributeFilter<>("DATE", "700000", new MockAttributeAccessor());

		boolean actual = JUnit4Util.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnit4Util.expect(false);
		assertThat("String attributeNotFound", actual, is(equalTo(expect)));

		boolean actual2 = JUnit4Util.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnit4Util.expect(false);
		assertThat("String attributeNotFound", actual2, is(equalTo(expect2)));
	}
}
