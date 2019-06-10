package org.afc.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class CombinationUtilTest {

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
	public void test4C2() {
		List<String> elements = Arrays.asList("A", "B", "C", "D"); 
		
		List<List<String>> actual = JUnitUtil.actual(CombinationUtil.generate(elements, 2));
		List<List<String>> expect = JUnitUtil.expect(
			Arrays.asList(
			    Arrays.asList("A", "B"),
			    Arrays.asList("A", "C"),
			    Arrays.asList("B", "C"),
			    Arrays.asList("A", "D"),
			    Arrays.asList("B", "D"),
			    Arrays.asList("C", "D")
			)
		);
		
		assertThat("size", actual.size(), is(equalTo(CombinationUtil.nCr(4, 2))));
		assertThat("content", actual, containsInAnyOrder(expect.toArray()));
	}

	@Test
	public void test6C3() {
		List<String> elements = Arrays.asList("A", "B", "C", "D", "E", "F"); 
		
		List<List<String>> actual = JUnitUtil.actual(CombinationUtil.generate(elements, 3));
		List<List<String>> expect = JUnitUtil.expect(
			Arrays.asList(
			    Arrays.asList("A", "B", "C"),
			    Arrays.asList("A", "B", "D"),
			    Arrays.asList("A", "C", "D"),
			    Arrays.asList("B", "C", "D"),
			    Arrays.asList("A", "B", "E"),
			    Arrays.asList("A", "C", "E"),
			    Arrays.asList("B", "C", "E"),
			    Arrays.asList("A", "D", "E"),
			    Arrays.asList("B", "D", "E"),
			    Arrays.asList("C", "D", "E"),
			    Arrays.asList("A", "B", "F"),
			    Arrays.asList("A", "C", "F"),
			    Arrays.asList("B", "C", "F"),
			    Arrays.asList("A", "D", "F"),
			    Arrays.asList("B", "D", "F"),
			    Arrays.asList("C", "D", "F"),
			    Arrays.asList("A", "E", "F"),
			    Arrays.asList("B", "E", "F"),
			    Arrays.asList("C", "E", "F"),
			    Arrays.asList("D", "E", "F")
			)
		);
		
		assertThat("size", actual.size(), is(equalTo(CombinationUtil.nCr(6, 3))));
		assertThat("content", actual, containsInAnyOrder(expect.toArray()));
	}
}
