package org.afc.jsse;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.afc.AFCException;

public class SSLContextFactory {

	private static final String JAVAX_NET_SSL_KEYSTORE = "javax.net.ssl.keyStore";

	private static final String JAVAX_NET_SSL_KEYSTOREPASSWORD = "javax.net.ssl.keyStorePassword";

	private static final String JAVAX_NET_SSL_KEYSTORETYPE   = "javax.net.ssl.keyStoreType";

	private static final String SSL_KEYMANAGERFACTORY_ALGORITHM   = "ssl.KeyManagerFactory.algorithm";

	private static final String JAVAX_NET_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";

	private static final String JAVAX_NET_SSL_TRUSTSTOREPASSWORD = "javax.net.ssl.trustStorePassword";

	private static final String JAVAX_NET_SSL_TRUSTSTORETYPE = "javax.net.ssl.trustStoreType";

	private static final String SSL_TRUSTMANAGERFACTORY_ALGORITHM = "ssl.TrustManagerFactory.algorithm";
	
	public static SSLContext createDefault(Map properties) {
		try {
			SSLContext sslContext = SSLContext.getDefault();
			KeyManagerFactory kmf = createKeyManagerFactory(properties);
			TrustManagerFactory tmf = createTrustManagerFactory(properties);
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
			return sslContext;
		} catch (GeneralSecurityException | IOException | URISyntaxException e) {
			throw new AFCException("unable to create new SSLContext", e);
		}
	}

	public static SSLContext createWithDisableTrustManager(Map properties) {
		try {
			SSLContext sslContext = SSLContext.getDefault();
			KeyManagerFactory kmf = createKeyManagerFactory(properties);
			TrustManagerFactory tmf = createTrustManagerFactory(properties);
			
			List<TrustManager> tm = Arrays.asList(tmf.getTrustManagers());
			tm.add(new DisableValidationTrustManager());
			
			sslContext.init(kmf.getKeyManagers(),  tm.toArray(new TrustManager[0]), new SecureRandom());
			return sslContext;
		} catch (GeneralSecurityException | IOException | URISyntaxException e) {
			throw new AFCException("unable to create new SSLContext", e);
		}
	}
	
	private static TrustManagerFactory createTrustManagerFactory(Map properties)
		throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, MalformedURLException, URISyntaxException {
		String trustStoreType = properties.getOrDefault(JAVAX_NET_SSL_TRUSTSTORETYPE, KeyStore.getDefaultType()).toString();
		String trustStoreFile = properties.get(JAVAX_NET_SSL_TRUSTSTORE).toString();
		String trustStorePassword = properties.get(JAVAX_NET_SSL_TRUSTSTOREPASSWORD).toString();
		String tmfAlgo = properties.getOrDefault(SSL_TRUSTMANAGERFACTORY_ALGORITHM, TrustManagerFactory.getDefaultAlgorithm()).toString();
		
		KeyStore trustStore = KeyStore.getInstance(trustStoreType);
		trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword.toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgo);
		tmf.init(trustStore);
		return tmf;
	}

	private static KeyManagerFactory createKeyManagerFactory(Map properties)
		throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, MalformedURLException, URISyntaxException, UnrecoverableKeyException {
		String keyStoreType = properties.getOrDefault(JAVAX_NET_SSL_KEYSTORETYPE, KeyStore.getDefaultType()).toString();
		String keyStoreFile = properties.get(JAVAX_NET_SSL_KEYSTORE).toString();
		String keyStorePassword = properties.get(JAVAX_NET_SSL_KEYSTOREPASSWORD).toString();
		String kmfAlgo = properties.getOrDefault(SSL_KEYMANAGERFACTORY_ALGORITHM, KeyManagerFactory.getDefaultAlgorithm()).toString();
		
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(new FileInputStream(keyStoreFile), keyStorePassword.toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(kmfAlgo);
		kmf.init(keyStore, keyStorePassword.toCharArray());
		return kmf;
	}
}
