package org.afc.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FileUtilTest {

	private String workingDir = toNativePath("D:\\workspace\\git\\study\\study-swagger");

	@Test
	public void testSelf() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.getRelativePath(workingDir, toNativePath("D:\\workspace\\git\\study\\study-swagger\\pom.xml"))), 
			is(JUnit4Util.expect(toNativePath("pom.xml")))
		);
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testParent() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.getRelativePath(workingDir, toNativePath("D:\\\\workspace\\\\git\\\\pom.xml"))), 
			is(JUnit4Util.expect(toNativePath("..\\..\\pom.xml")))
		);
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testChild() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.getRelativePath(workingDir, toNativePath("D:\\workspace\\git\\study\\study-swagger-api\\target\\pom.xml"))), 
			is(JUnit4Util.expect(toNativePath("..\\study-swagger-api\\target\\pom.xml")))
		);
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testSibling() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.getRelativePath(workingDir, toNativePath("D:\\workspace\\git\\study-swagger-api\\pom.xml"))), 
			is(JUnit4Util.expect(toNativePath("..\\..\\study-swagger-api\\pom.xml")))
		);
		JUnit4Util.endCurrentTest(getClass());
	}
	
	@Test
	public void testClasspathProtocol() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.resolveAbsolutePath("classpath://ugly-keystore.jks")), 
			is(JUnit4Util.expect(System.getProperty("user.dir") + toNativePath("\\target\\test-classes\\ugly-keystore.jks")))
		); 
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testClasspathProtocolNotFound() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.resolveAbsolutePath("file://target/test-classes/ugly-keystore-not-found.jks")), 
			is(JUnit4Util.expect(nullValue()))
		); 
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testFileProtocol() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.resolveAbsolutePath("file://target/test-classes/ugly-keystore.jks")), 
			is(JUnit4Util.expect(System.getProperty("user.dir") + toNativePath("\\target\\test-classes\\ugly-keystore.jks")))
		); 
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testFileProtocolNotFound() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.resolveAbsolutePath("file://target/test-classes/ugly-keystore-not-found.jks")), 
			is(JUnit4Util.expect(nullValue()))
		); 
		JUnit4Util.endCurrentTest(getClass());
	}

	@Test
	public void testFilePath() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.resolveAbsolutePath("target/test-classes/ugly-keystore.jks")), 
			is(JUnit4Util.expect(System.getProperty("user.dir") + toNativePath("\\target\\test-classes\\ugly-keystore.jks")))
		); 
		
		JUnit4Util.endCurrentTest(getClass());
	}
	
	@Test
	public void testFilePathNotFound() throws IOException {
		JUnit4Util.startCurrentTest(getClass());
		assertThat(
			JUnit4Util.actual(FileUtil.resolveAbsolutePath("target/test-classes/ugly-keystore-not-found.jks")), 
			is(JUnit4Util.expect(nullValue()))
		); 
		
		JUnit4Util.endCurrentTest(getClass());
	}

	private static String toNativePath(String path) {
		return path.replaceAll("[\\\\|/]", '\\' + File.separator);
	}
}