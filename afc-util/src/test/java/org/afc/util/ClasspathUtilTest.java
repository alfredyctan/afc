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

public class ClasspathUtilTest {

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
	public void testInJar() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.dom4j.rule", true, true));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
			Class.forName("org.dom4j.rule.Action"),
			Class.forName("org.dom4j.rule.Mode"),
			Class.forName("org.dom4j.rule.NullAction"),
			Class.forName("org.dom4j.rule.pattern.DefaultPattern"),
			Class.forName("org.dom4j.rule.pattern.NodeTypePattern"),
			Class.forName("org.dom4j.rule.Pattern"),
			Class.forName("org.dom4j.rule.Rule"),
			Class.forName("org.dom4j.rule.RuleManager$1"),
			Class.forName("org.dom4j.rule.RuleManager"),
			Class.forName("org.dom4j.rule.RuleSet"),
			Class.forName("org.dom4j.rule.Stylesheet")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));
	}


	@Test
	public void testInJarBaseOnly() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.dom4j.rule", false, true));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
			Class.forName("org.dom4j.rule.Action"),
			Class.forName("org.dom4j.rule.Mode"),
			Class.forName("org.dom4j.rule.NullAction"),
			Class.forName("org.dom4j.rule.Pattern"),
			Class.forName("org.dom4j.rule.Rule"),
			Class.forName("org.dom4j.rule.RuleManager$1"),
			Class.forName("org.dom4j.rule.RuleManager"),
			Class.forName("org.dom4j.rule.RuleSet"),
			Class.forName("org.dom4j.rule.Stylesheet")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));
	}

	@Test
	public void testInJarNoInner() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.dom4j.rule", true, false));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
			Class.forName("org.dom4j.rule.Action"),
			Class.forName("org.dom4j.rule.Mode"),
			Class.forName("org.dom4j.rule.NullAction"),
			Class.forName("org.dom4j.rule.pattern.DefaultPattern"),
			Class.forName("org.dom4j.rule.pattern.NodeTypePattern"),
			Class.forName("org.dom4j.rule.Pattern"),
			Class.forName("org.dom4j.rule.Rule"),
			Class.forName("org.dom4j.rule.RuleManager"),
			Class.forName("org.dom4j.rule.RuleSet"),
			Class.forName("org.dom4j.rule.Stylesheet")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));

	}

	@Test
	public void testInJarBaseOnlyNoInner() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.dom4j.rule", false, false));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
			Class.forName("org.dom4j.rule.Action"),
			Class.forName("org.dom4j.rule.Mode"),
			Class.forName("org.dom4j.rule.NullAction"),
			Class.forName("org.dom4j.rule.Pattern"),
			Class.forName("org.dom4j.rule.Rule"),
			Class.forName("org.dom4j.rule.RuleManager"),
			Class.forName("org.dom4j.rule.RuleSet"),
			Class.forName("org.dom4j.rule.Stylesheet")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));
	}


	@Test
	public void testInDir() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.afc.util.test", true, true));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
			Class.forName("org.afc.util.test.Base$Inner"),
		    Class.forName("org.afc.util.test.Base"),
		    Class.forName("org.afc.util.test.sub.Sub$Inner"),
		    Class.forName("org.afc.util.test.sub.Sub")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));
	}

	@Test
	public void testInDirBaseOnly() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.afc.util.test", false, true));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
		    Class.forName("org.afc.util.test.Base$Inner"),
		    Class.forName("org.afc.util.test.Base")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));
	}

	@Test
	public void testInDirNoInner() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.afc.util.test", true, false));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
		    Class.forName("org.afc.util.test.Base"),
		    Class.forName("org.afc.util.test.sub.Sub")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));
	}

	@Test
	public void testInDirBaseOnlyNoInner() throws ClassNotFoundException {
		List<Class<?>> actual = JUnit4Util.actual(ClasspathUtil.findClasses("org.afc.util.test", false, false));
		List<Class<?>> expect = JUnit4Util.expect(Arrays.asList(
		    Class.forName("org.afc.util.test.Base")
		));

		assertThat("in jar", actual, containsInAnyOrder(expect.toArray()));
	}
}
