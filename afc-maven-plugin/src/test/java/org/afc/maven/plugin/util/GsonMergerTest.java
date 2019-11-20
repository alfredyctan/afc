package org.afc.maven.plugin.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.afc.util.FileUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import afc.org.maven.plugin.util.GsonMerger;


public class GsonMergerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		GsonMerger.merge("target/merge.json", "src/test/resources/source1.json", "src/test/resources/source2.json", "src/test/resources/common.json");
		String actual = FileUtil.readFileAsString("target/merge.json");
		String expect = FileUtil.readFileAsString("src/test/resources/expect.json");
		assertThat("", actual, is(equalTo(expect)));
	}
}
