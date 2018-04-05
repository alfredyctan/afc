package org.afc.jsse;

public class JSSEProperties {

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
}
