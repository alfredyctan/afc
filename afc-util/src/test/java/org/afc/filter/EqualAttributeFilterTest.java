package org.afc.filter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.util.JUnitUtil;

public class EqualAttributeFilterTest {

	@Rule
	public TestName name = new TestName();
	
	private EqualAttributeFilter<Map<String, String>> filter;

	@Before
	public void setUp() throws Exception {
		JUnitUtil.startCurrentTest(getClass(), name);
	}
	
	@After
	public void tearDown() throws Exception {
		JUnitUtil.describe(filter);
		JUnitUtil.endCurrentTest(getClass(), name);
	}
	
	@Test
	public void testString() throws Exception {
		filter = new EqualAttributeFilter<>("STATUS", "LIVE", new MockAttributeAccessor());

		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(true);
		assertThat("String equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("String equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testNumeric() throws Exception {
		filter = new EqualAttributeFilter<>("AMOUNT", "1000000", new MockAttributeAccessor());

		AttributeFilter<?> actual0 = JUnitUtil.actual(filter.delegate);
		Class<?> expect0 = JUnitUtil.expect(EqualAttributeFilter.NumericFilter.class);
		assertThat("Filter type ", actual0, is(instanceOf(expect0)));

		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(true);
		assertThat("Numeric equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("Numeric equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testLocalDate() throws Exception {
		filter = new EqualAttributeFilter<>("LOCALDATE", "2018-06-28", new MockAttributeAccessor());

		AttributeFilter<?> actual0 = JUnitUtil.actual(filter.delegate);
		Class<?> expect0 = JUnitUtil.expect(EqualAttributeFilter.LocalDateFilter.class);
		assertThat("Filter type ", actual0, is(instanceOf(expect0)));
		
		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(true);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testLocalTime() throws Exception {
		filter = new EqualAttributeFilter<>("LOCALTIME", "10:15:30", new MockAttributeAccessor());

		AttributeFilter<?> actual0 = JUnitUtil.actual(filter.delegate);
		Class<?> expect0 = JUnitUtil.expect(EqualAttributeFilter.LocalTimeFilter.class);
		assertThat("Filter type ", actual0, is(instanceOf(expect0)));
		
		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(true);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testLocalDateTime() throws Exception {
		filter = new EqualAttributeFilter<>("LOCALDATETIME", "2018-06-28T10:15:30", new MockAttributeAccessor());

		AttributeFilter<?> actual0 = JUnitUtil.actual(filter.delegate);
		Class<?> expect0 = JUnitUtil.expect(EqualAttributeFilter.LocalDateTimeFilter.class);
		assertThat("Filter type ", actual0, is(instanceOf(expect0)));
		
		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(true);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testOffsetDateTime() throws Exception {
		filter = new EqualAttributeFilter<>("OFFSETDATETIME", "2018-06-28T10:15:30+08:00", new MockAttributeAccessor());

		AttributeFilter<?> actual0 = JUnitUtil.actual(filter.delegate);
		Class<?> expect0 = JUnitUtil.expect(EqualAttributeFilter.OffsetDateTimeFilter.class);
		assertThat("Filter type ", actual0, is(instanceOf(expect0)));
		
		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(true);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testZonedDateTime() throws Exception {
		filter = new EqualAttributeFilter<>("ZONEDDATETIME", "2018-06-28T10:15:30+08:00[Asia/Hong_Kong]", new MockAttributeAccessor());

		AttributeFilter<?> actual0 = JUnitUtil.actual(filter.delegate);
		Class<?> expect0 = JUnitUtil.expect(EqualAttributeFilter.ZonedDateTimeFilter.class);
		assertThat("Filter type ", actual0, is(instanceOf(expect0)));
		
		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(true);
		assertThat("Date equal match ", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("Date equal not match", actual2, is(equalTo(expect2)));
	}
	

	@Test
	public void testAttributeNotFound() throws Exception {
		filter = new EqualAttributeFilter<>("notexist", "?", new MockAttributeAccessor());

		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(false);
		assertThat("attributeNotFound", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("attributeNotFound", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testDateTypeMismatch() throws Exception {
		filter = new EqualAttributeFilter<>("AMOUNT", "2018-06-28", new MockAttributeAccessor());

		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(false);
		assertThat("String attributeNotFound", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("String attributeNotFound", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testNumericTypeMismatch() throws Exception {
		filter = new EqualAttributeFilter<>("DATE", "1000000", new MockAttributeAccessor());

		boolean actual = JUnitUtil.actual(filter.filter(AttributeMock.attributesLive()));
		boolean expect = JUnitUtil.expect(false);
		assertThat("String attributeNotFound", actual, is(equalTo(expect)));

		boolean actual2 = JUnitUtil.actual(filter.filter(AttributeMock.attributesClosed()));
		boolean expect2 = JUnitUtil.expect(false);
		assertThat("String attributeNotFound", actual2, is(equalTo(expect2)));
	}
}
