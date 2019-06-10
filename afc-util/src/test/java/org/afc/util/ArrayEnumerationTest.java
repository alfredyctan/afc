package org.afc.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrayEnumerationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		JUnit4Util.startCurrentTest(getClass());

		List<Integer> actual = new LinkedList<>();
		for (Enumeration<Integer> e = new ArrayEnumeration<>(new Integer[] { 1, 2, 3, 4 }); e.hasMoreElements();) {
			actual.add(e.nextElement());
		}
		JUnit4Util.actual(actual);
		List<Integer> expect = JUnit4Util.expect(ListUtil.list(new LinkedList<>(), 1, 2, 3, 4));

		assertThat("Equals in Order", actual, contains(expect.toArray()));

		JUnit4Util.endCurrentTest(getClass());
	}

}
