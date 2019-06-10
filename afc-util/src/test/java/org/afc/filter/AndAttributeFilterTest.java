package org.afc.filter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mockito.Mockito;

import org.afc.util.JUnit4Util;

public class AndAttributeFilterTest {

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
	public void testOneFalse() throws Exception {
		AttributeFilter<Object> subFilter1 = Mockito.mock(AttributeFilter.class);
		AttributeFilter<Object> subFilter2 = Mockito.mock(AttributeFilter.class);
		when(subFilter1.filter(any())).thenReturn(true);
		when(subFilter2.filter(any())).thenReturn(false);
		
		AndAttributeFilter<Object> filter1 = new AndAttributeFilter<>(Arrays.asList(subFilter1, subFilter2));
		boolean actual1 = JUnit4Util.actual(filter1.filter(new Object()));
		boolean expect1 = JUnit4Util.actual(false);
		assertThat("And match ", actual1, is(equalTo(expect1)));

		AndAttributeFilter<Object> filter2 = new AndAttributeFilter<>(Arrays.asList(subFilter2, subFilter1));
		boolean actual2 = JUnit4Util.actual(filter2.filter(new Object()));
		boolean expect2 = JUnit4Util.actual(false);
		assertThat("And match ", actual2, is(equalTo(expect2)));
	}

	@Test
	public void testAllTrue() throws Exception {
		AttributeFilter<Object> subFilter1 = Mockito.mock(AttributeFilter.class);
		AttributeFilter<Object> subFilter2 = Mockito.mock(AttributeFilter.class);
		when(subFilter1.filter(any())).thenReturn(true);
		when(subFilter2.filter(any())).thenReturn(true);
		
		AndAttributeFilter<Object> filter1 = new AndAttributeFilter<>(Arrays.asList(subFilter1, subFilter2));
		boolean actual1 = JUnit4Util.actual(filter1.filter(new Object()));
		boolean expect1 = JUnit4Util.actual(true);
		assertThat("And match ", actual1, is(equalTo(expect1)));

		AndAttributeFilter<Object> filter2 = new AndAttributeFilter<>(Arrays.asList(subFilter2, subFilter1));
		boolean actual2 = JUnit4Util.actual(filter2.filter(new Object()));
		boolean expect2 = JUnit4Util.actual(true);
		assertThat("And match ", actual2, is(equalTo(expect2)));
	}
}
