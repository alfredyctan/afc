package org.afc.servlet;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.afc.util.ArrayEnumeration;
import org.afc.util.JUnit4Util;
import org.afc.util.ListUtil;


public class AugmentedHeaderHttpServletRequestWrapperTest {

	private JUnit4Mockery context;

	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery();
	}

	@After
	public void tearDown() throws Exception {
		context.assertIsSatisfied();
	}
	
	@Test
	public void testGetDateHeader() throws ParseException {
		JUnit4Util.startCurrentTest(getClass());

		HttpServletRequest request = context.mock(HttpServletRequest.class);
		AugmentedHeaderHttpServletRequestWrapper wrapper = new AugmentedHeaderHttpServletRequestWrapper(request);

		context.checking(new Expectations() {{
			oneOf(request).getDateHeader("Original");
			will(returnValue(1000L));
		}}); 

		wrapper.addHeader("Date1", "Thu, 31 May 2007 08:49:37 GMT");
		wrapper.addHeader("Date2", "Thursday, 31-May-07 08:49:37 GMT");
		wrapper.addHeader("Date3", "Thu May 31 08:49:37 2007");

		long d1 = wrapper.getDateHeader("Date1");
		long d2 = wrapper.getDateHeader("Date2");
		long d3 = wrapper.getDateHeader("Date3");
		long d4 = wrapper.getDateHeader("Original");
		
		long e3 = new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy").parse("Thu May 31 08:49:37 2007").getTime();
		
		assertThat("format : EEE, dd MMM yyyy HH:mm:ss zzz", d1, is(equalTo(1180601377000L)));
		assertThat("format : EEEEEE, dd-MMM-yy HH:mm:ss zzz", d2, is(equalTo(1180601377000L)));
		assertThat("format : EEE MMMM d HH:mm:ss yyyy", d3, is(equalTo(e3)));
		assertThat("from original", d4, is(equalTo(1000L)));
		
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testGetIntHeader() throws ParseException {
		JUnit4Util.startCurrentTest(getClass());

		HttpServletRequest request = context.mock(HttpServletRequest.class);
		AugmentedHeaderHttpServletRequestWrapper wrapper = new AugmentedHeaderHttpServletRequestWrapper(request);

		context.checking(new Expectations() {{
			oneOf(request).getIntHeader("Original");
			will(returnValue(10));
		}}); 

		wrapper.addHeader("Int1", "1");
		wrapper.addHeader("Int2", "2");
		wrapper.addHeader("Int3", "3");

		int actual1 = wrapper.getIntHeader("Int1");
		int actual2 = wrapper.getIntHeader("Int2");
		int actual3 = wrapper.getIntHeader("Int3");
		int actual4 = wrapper.getIntHeader("Original");
		
		assertThat("Int1", actual1, is(equalTo(1)));
		assertThat("Int2", actual2, is(equalTo(2)));
		assertThat("Int3", actual3, is(equalTo(3)));
		assertThat("from original", actual4, is(equalTo(10)));
		
		JUnit4Util.endCurrentTest(getClass());
	}	
	@Test
	public void testGetHeader() throws ParseException {
		JUnit4Util.startCurrentTest(getClass());

		HttpServletRequest request = context.mock(HttpServletRequest.class);
		AugmentedHeaderHttpServletRequestWrapper wrapper = new AugmentedHeaderHttpServletRequestWrapper(request);

		context.checking(new Expectations() {{
			oneOf(request).getHeader("Original");
			will(returnValue("From Original"));
		}}); 

		wrapper.addHeader("Date1", "Thu, 31 May 2007 08:49:37 GMT");
		wrapper.addHeader("Date1", "Thursday, 31-May-07 08:49:37 GMT");
		wrapper.addHeader("Date1", "Thu May 31 08:49:37 2007");

		String actual1 = wrapper.getHeader("Date1");
		String actual2 = wrapper.getHeader("Date1");
		String actual3 = wrapper.getHeader("Date1");
		String actual4 = wrapper.getHeader("Original");
		
		
		assertThat("1st", actual1, is(equalTo("Thu, 31 May 2007 08:49:37 GMT")));
		assertThat("2nd", actual2, is(equalTo("Thu, 31 May 2007 08:49:37 GMT")));
		assertThat("3rd", actual3, is(equalTo("Thu, 31 May 2007 08:49:37 GMT")));
		assertThat("from original", actual4, is(equalTo("From Original")));
		
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testGetHeaderNames() throws ParseException {
		JUnit4Util.startCurrentTest(getClass());

		HttpServletRequest request = context.mock(HttpServletRequest.class);
		AugmentedHeaderHttpServletRequestWrapper wrapper = new AugmentedHeaderHttpServletRequestWrapper(request);

		context.checking(new Expectations() {{
			oneOf(request).getHeaderNames();
			will(returnValue(new ArrayEnumeration<>(new String[] {"Original"})));
		}}); 

		wrapper.addHeader("Date1", "Thu, 31 May 2007 08:49:37 GMT");
		wrapper.addHeader("Date2", "Thursday, 31-May-07 08:49:37 GMT");
		wrapper.addHeader("Date3", "Thu May 31 08:49:37 2007");

		List<String> actual = new LinkedList<>();
		for (Enumeration<String> e = wrapper.getHeaderNames(); e.hasMoreElements(); ) {
			actual.add(e.nextElement());
		}

		JUnit4Util.actual(actual);
		List<String> expect = JUnit4Util.expect(ListUtil.list(new LinkedList<>(), "Original", "Date1", "Date2", "Date3"));

		assertThat("Equals in Order", actual, contains(expect.toArray()));
		
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testGetHeaders() throws ParseException {
		JUnit4Util.startCurrentTest(getClass());

		HttpServletRequest request = context.mock(HttpServletRequest.class);
		AugmentedHeaderHttpServletRequestWrapper wrapper = new AugmentedHeaderHttpServletRequestWrapper(request);

		context.checking(new Expectations() {{
			oneOf(request).getHeaders("Original");
			will(returnValue(new ArrayEnumeration<>(new String[] {"From Original"})));
		}}); 

		wrapper.addHeader("Date1", "Thu, 31 May 2007 08:49:37 GMT");
		wrapper.addHeader("Date1", "Thursday, 31-May-07 08:49:37 GMT");
		wrapper.addHeader("Date1", "Thu May 31 08:49:37 2007");

		List<String> actual = new LinkedList<>();
		for (Enumeration<String> e = wrapper.getHeaders("Date1"); e.hasMoreElements(); ) {
			actual.add(e.nextElement());
		}
		JUnit4Util.actual(actual);
		List<String> expect = JUnit4Util.expect(ListUtil.list(new LinkedList<>(), "Thu, 31 May 2007 08:49:37 GMT", "Thursday, 31-May-07 08:49:37 GMT", "Thu May 31 08:49:37 2007"));
		assertThat("Date1 ", actual, contains(expect.toArray()));
		
		List<String> actual2 = new LinkedList<>();
		for (Enumeration<String> e = wrapper.getHeaders("Original"); e.hasMoreElements(); ) {
			actual2.add(e.nextElement());
		}
		JUnit4Util.actual(actual2);
		List<String> expect2 = JUnit4Util.expect(ListUtil.list(new LinkedList<>(), "From Original"));
		assertThat("Date1 ", actual2, contains(expect2.toArray()));
		
		
		JUnit4Util.endCurrentTest(getClass());
	}
}
