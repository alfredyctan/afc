package org.afc.jsse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.util.FileUtil;

public class JSSEProperties {

	private static final Logger logger = LoggerFactory.getLogger(JSSEProperties.class);
	
	public static final String JAVAX_NET_SSL_KEYSTORE = "javax.net.ssl.keyStore";

	public static final String JAVAX_NET_SSL_KEYSTOREPASSWORD = "javax.net.ssl.keyStorePassword";

	public static final String JAVAX_NET_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";

	public static final String JAVAX_NET_SSL_TRUSTSTOREPASSWORD = "javax.net.ssl.trustStorePassword";

	public static final String JAVAX_NET_DEBUG = "javax.net.debug";
	
	public static void setKeyStore(String store, String storepass) {
		System.setProperty(JAVAX_NET_SSL_KEYSTORE, store);
		System.setProperty(JAVAX_NET_SSL_KEYSTOREPASSWORD, storepass);
	}

	public static void setTrustStore(String store, String storepass) {
		System.setProperty(JAVAX_NET_SSL_TRUSTSTORE, store);
		System.setProperty(JAVAX_NET_SSL_TRUSTSTOREPASSWORD, storepass);
	}

	public static void setDebug(String logger) {
		System.setProperty(JAVAX_NET_DEBUG, logger);
	}

	public static void resolve() {
		String keystorePath = System.getProperty(JAVAX_NET_SSL_KEYSTORE);
		if (keystorePath != null) {
			String newKeystorePath = FileUtil.resolveAbsolutePath(keystorePath);
			if (newKeystorePath != null) {
				System.setProperty(JAVAX_NET_SSL_KEYSTORE, newKeystorePath);
				logger.info("keystore path resolved : {} -> {}", keystorePath, newKeystorePath);
			}
		}
		String truststorePath = System.getProperty(JAVAX_NET_SSL_TRUSTSTORE);
		if (truststorePath != null) {
			String newTruststorePath = FileUtil.resolveAbsolutePath(truststorePath);
			if (newTruststorePath != null) {
				System.setProperty(JAVAX_NET_SSL_TRUSTSTORE, newTruststorePath);
				logger.info("truststore path resolved : {} -> {}", truststorePath, newTruststorePath);
			}
		}
	}
}
