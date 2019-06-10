package org.afc.jsse;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.Test;

import org.afc.util.JUnit4Util;


public class JSSEPropertiesTest {

	@Test
	public void test() {
		JUnit4Util.startCurrentTest(getClass());
		JSSEProperties.setKeyStore("classpath://ugly-keystore.jks", "afc");
		JSSEProperties.setTrustStore("classpath://ugly-keystore.jks", "afc");
		JSSEProperties.resolve();
		
		assertThat(
			JUnit4Util.actual(System.getProperty(JSSEProperties.JAVAX_NET_SSL_KEYSTORE).replaceAll('\\' + File.separator + "clover", "")), 
			is(JUnit4Util.expect(System.getProperty("user.dir") + toNativePath("\\target\\test-classes\\ugly-keystore.jks")))
		);
		assertThat(
			JUnit4Util.actual(System.getProperty(JSSEProperties.JAVAX_NET_SSL_TRUSTSTORE).replaceAll('\\' + File.separator + "clover", "")), 
			is(JUnit4Util.expect(System.getProperty("user.dir") + toNativePath("\\target\\test-classes\\ugly-keystore.jks")))
		);
		
		JUnit4Util.endCurrentTest(getClass());
	}

	private static String toNativePath(String path) {
		return path.replaceAll("[\\\\|/]", '\\' + File.separator);
	}
}
