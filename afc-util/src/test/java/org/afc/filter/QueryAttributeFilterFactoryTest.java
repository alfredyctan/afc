package org.afc.filter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.AFCException;
import org.afc.util.JUnitUtil;

public class QueryAttributeFilterFactoryTest {
	
	@Rule
	public TestName name = new TestName();

	@Before
	public void setUp() throws Exception {
		JUnitUtil.startCurrentTest(getClass(), name);
	}
	@After
	public void tearDown() throws Exception {
		JUnitUtil.endCurrentTest(getClass(), name);
	}

	@Test
	public void testNested() throws Exception {
		QueryAttributeFilterFactory<Map<String, String>> factory = new QueryAttributeFilterFactory<>(new ExpressionAttributeFilterFactory<>(new MockAttributeAccessor()));
		
		AttributeFilter<Map<String, String>> actual = JUnitUtil.actual(factory.create("((35=R) && (10>100)) || ((35=A) && (10<=200)) || (35=0)"));
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(
			new OrAttributeFilter<>(Arrays.asList( 
					new AndAttributeFilter<>(Arrays.asList(
						new EqualAttributeFilter<>("35", "R", new MockAttributeAccessor()),
						new GreaterAttributeFilter<>("10", "100", new MockAttributeAccessor())
					)),
					new AndAttributeFilter<>(Arrays.asList(
						new EqualAttributeFilter<>("35", "A", new MockAttributeAccessor()),
						new LesserEqualAttributeFilter<>("10", "200", new MockAttributeAccessor())
					)),
					new EqualAttributeFilter<>("35", "0", new MockAttributeAccessor())
			))
		);

		assertThat("nested filter", actual, is(equalTo(expect)));
	}

	@Test
	public void testInconsistent() throws Exception {
		QueryAttributeFilterFactory<Map<String, String>> factory = new QueryAttributeFilterFactory<>(new ExpressionAttributeFilterFactory<>(new MockAttributeAccessor()));
		
		try {
			factory.create("((35=R) && (10>100)) || ((35=A) && (10<=200)) && (35=0)");
			fail("should throw exception");
		} catch (AFCException e) {
			e.printStackTrace();
			String actual = JUnitUtil.actual(e.getMessage());
			String expect = "[ && ] is invalid, pos : 45 - 49";
			assertThat("expect inconsistent exception", actual, is(equalTo(expect)));
		}
	}
	
	@Test
	public void testInvalid() throws Exception {
		QueryAttributeFilterFactory<Map<String, String>> factory = new QueryAttributeFilterFactory<>(new ExpressionAttributeFilterFactory<>(new MockAttributeAccessor()));
		
		try {
			factory.create("((35=R) && (10>100)) || ((35=A) && (10<=200) && (35=0)");
			fail("should throw exception");
		} catch (AFCException e) {
			e.printStackTrace();
			String actual = JUnitUtil.actual(e.getMessage());
			String expect = "syntax error: [ || ((35=A) && (10<=200) && (35=0)]";
			assertThat("expect invalid exception", actual, is(equalTo(expect)));
		}
	}
	
	@Test
	public void testInvalid2() throws Exception {
		QueryAttributeFilterFactory<Map<String, String>> factory = new QueryAttributeFilterFactory<>(new ExpressionAttributeFilterFactory<>(new MockAttributeAccessor()));
		
		try {
			factory.create("((35=R) && (10>100)) || ((35=A) && 10<=200) && (35=0)");
			fail("should throw exception");
		} catch (AFCException e) {
			e.printStackTrace();
			String actual = JUnitUtil.actual(e.getMessage());
			String expect = "syntax error: [ && 10<=200]";
			assertThat("expect invalid exception", actual, is(equalTo(expect)));
		}
	}
}
