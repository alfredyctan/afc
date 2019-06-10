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

public class ExpressionAttributeFilterFactoryTest {

	@Rule
	public TestName name = new TestName();

	private ExpressionAttributeFilterFactory<Map<String, String>> factory = new ExpressionAttributeFilterFactory<>(new MockAttributeAccessor());

	private AttributeAccessor<Map<String, String>> accessor = new MockAttributeAccessor();
	
	@Before
	public void setUp() throws Exception {
		JUnitUtil.startCurrentTest(getClass(), name);
	}

	@After
	public void tearDown() throws Exception {
		JUnitUtil.endCurrentTest(getClass(), name);
	}

	@Test
	public void testEqual() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new EqualAttributeFilter<>("10", "20", accessor));

		AttributeFilter<Map<String, String>> actual1 = JUnitUtil.actual(factory.create("10=20"));
		assertThat("filter without padding whitespace", actual1, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual2 = JUnitUtil.actual(factory.create("10 =20"));
		assertThat("filter with leading padding whitespace", actual2, is(equalTo(expect)));

		AttributeFilter<Map<String, String>> actual3 = JUnitUtil.actual(factory.create("10= 20"));
		assertThat("filter with trailing padding whitespace", actual3, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 = 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testNotEqual() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new NotAttributeFilter<>(new EqualAttributeFilter<>("10", "20", accessor)));

		AttributeFilter<Map<String, String>> actual1 = JUnitUtil.actual(factory.create("10!=20"));
		assertThat("filter without padding whitespace", actual1, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual2 = JUnitUtil.actual(factory.create("10 !=20"));
		assertThat("filter with leading padding whitespace", actual2, is(equalTo(expect)));

		AttributeFilter<Map<String, String>> actual3 = JUnitUtil.actual(factory.create("10!= 20"));
		assertThat("filter with trailing padding whitespace", actual3, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 != 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}
	
	@Test
	public void testGreater() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new GreaterAttributeFilter<>("10", "20", accessor));

		AttributeFilter<Map<String, String>> actual1 = JUnitUtil.actual(factory.create("10>20"));
		assertThat("filter without padding whitespace", actual1, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual2 = JUnitUtil.actual(factory.create("10 >20"));
		assertThat("filter with leading padding whitespace", actual2, is(equalTo(expect)));

		AttributeFilter<Map<String, String>> actual3 = JUnitUtil.actual(factory.create("10> 20"));
		assertThat("filter with trailing padding whitespace", actual3, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 > 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testGreaterEqual() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new GreaterEqualAttributeFilter<>("10", "20", accessor));

		AttributeFilter<Map<String, String>> actual1 = JUnitUtil.actual(factory.create("10>=20"));
		assertThat("filter without padding whitespace", actual1, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual2 = JUnitUtil.actual(factory.create("10 >=20"));
		assertThat("filter with leading padding whitespace", actual2, is(equalTo(expect)));

		AttributeFilter<Map<String, String>> actual3 = JUnitUtil.actual(factory.create("10>= 20"));
		assertThat("filter with trailing padding whitespace", actual3, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 >= 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testLesser() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new LesserAttributeFilter<>("10", "20", accessor));

		AttributeFilter<Map<String, String>> actual1 = JUnitUtil.actual(factory.create("10<20"));
		assertThat("filter without padding whitespace", actual1, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual2 = JUnitUtil.actual(factory.create("10 <20"));
		assertThat("filter with leading padding whitespace", actual2, is(equalTo(expect)));

		AttributeFilter<Map<String, String>> actual3 = JUnitUtil.actual(factory.create("10< 20"));
		assertThat("filter with trailing padding whitespace", actual3, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 < 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testLesserEqual() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new LesserEqualAttributeFilter<>("10", "20", accessor));

		AttributeFilter<Map<String, String>> actual1 = JUnitUtil.actual(factory.create("10<=20"));
		assertThat("filter without padding whitespace", actual1, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual2 = JUnitUtil.actual(factory.create("10 <=20"));
		assertThat("filter with leading padding whitespace", actual2, is(equalTo(expect)));

		AttributeFilter<Map<String, String>> actual3 = JUnitUtil.actual(factory.create("10<= 20"));
		assertThat("filter with trailing padding whitespace", actual3, is(equalTo(expect)));
		
		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 <= 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testLike() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new LikeAttributeFilter<>("10", "20", accessor::getString));

		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 like 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testNotLike() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new NotAttributeFilter<>(new LikeAttributeFilter<>("10", "20", accessor::getString)));

		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 !like 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testIn() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new InAttributeFilter<>("10", "20,30,40", accessor::getString));

		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 in 20,30,40"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}

	@Test
	public void testNotIn() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new NotAttributeFilter<>(new InAttributeFilter<>("10", "20,30,40", accessor::getString)));

		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 !in 20,30,40"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}
	
	@Test
	public void testRegex() throws Exception {
		AttributeFilter<Map<String, String>> expect = JUnitUtil.expect(new RegexAttributeFilter<>("10", "20", accessor::getString));

		AttributeFilter<Map<String, String>> actual4 = JUnitUtil.actual(factory.create("10 regex 20"));
		assertThat("filter with padding whitespace", actual4, is(equalTo(expect)));
	}
}
