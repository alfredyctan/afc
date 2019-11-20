package org.afc.maven.plugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;

import org.afc.util.FileUtil;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

import afc.org.maven.plugin.JsonMergeMojo;

public class JsonMergeMojoTest extends AbstractMojoTestCase {

	@Test
	public void testMerge() throws Exception {
		File pom = new File("src/test/resources/merge/pom.xml");
		assertNotNull(pom);
		assertTrue(pom.exists());
		JsonMergeMojo myMojo = (JsonMergeMojo) lookupMojo("json-merge", pom);
		assertNotNull(myMojo);
		myMojo.execute();

		String actual = FileUtil.readFileAsString("target/merge.json");
		String expect = FileUtil.readFileAsString("src/test/resources/expect.json");
		assertThat("", actual, is(equalTo(expect)));
	}
}
