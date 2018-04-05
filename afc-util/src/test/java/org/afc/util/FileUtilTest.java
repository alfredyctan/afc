package org.afc.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FileUtilTest {

	private String workingDir = "D:\\workspace\\git\\study\\study-swagger";

	@Test
	public void testSelf() throws IOException {
		assertThat(FileUtil.getRelativePath(workingDir, toNativePath("D:\\workspace\\git\\study\\study-swagger\\pom.xml")), is(toNativePath("pom.xml")));
	}

	@Test
	public void testParent() throws IOException {
		assertThat(FileUtil.getRelativePath(workingDir, toNativePath("D:\\\\workspace\\\\git\\\\pom.xml")), is(toNativePath("..\\..\\pom.xml")));
	}

	@Test
	public void testChild() throws IOException {
		assertThat(FileUtil.getRelativePath(workingDir, toNativePath("D:\\workspace\\git\\study\\study-swagger-api\\target\\pom.xml")), is(toNativePath("..\\study-swagger-api\\target\\pom.xml")));
	}

	@Test
	public void testSibling() throws IOException {
		assertThat(FileUtil.getRelativePath(workingDir, toNativePath("D:\\workspace\\git\\study-swagger-api\\pom.xml")), is(toNativePath("..\\..\\study-swagger-api\\pom.xml")));
	}
	

	private static String toNativePath(String path) {
		return path.replaceAll("[\\\\|/]", '\\' + File.separator);
	}
}
