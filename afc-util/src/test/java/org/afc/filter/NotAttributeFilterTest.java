package org.afc.filter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mockito.Mockito;

import org.afc.util.JUnit4Util;

public class NotAttributeFilterTest {

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
	public void testMatch() throws Exception {
		AttributeFilter<Object> subFilter1 = Mockito.mock(AttributeFilter.class);
		when(subFilter1.filter(any())).thenReturn(false);
		
		NotAttributeFilter<Object> filter1 = new NotAttributeFilter<>(subFilter1);
		boolean actual1 = JUnit4Util.actual(filter1.filter(new Object()));
		boolean expect1 = JUnit4Util.expect(true);
		assertThat("Not unmatch ", actual1, is(equalTo(expect1)));
	}

	@Test
	public void testUnMatch() throws Exception {
		AttributeFilter<Object> subFilter1 = Mockito.mock(AttributeFilter.class);
		when(subFilter1.filter(any())).thenReturn(true);
		
		NotAttributeFilter<Object> filter1 = new NotAttributeFilter<>(subFilter1);
		boolean actual1 = JUnit4Util.actual(filter1.filter(new Object()));
		boolean expect1 = JUnit4Util.expect(false);
		assertThat("Not match ", actual1, is(equalTo(expect1)));

	}
}
