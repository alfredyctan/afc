package org.afc.jsse;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.X509KeyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelegatedKeyManagerFactorySpi extends KeyManagerFactorySpi {

	private static final Logger logger = LoggerFactory.getLogger(DelegatedKeyManagerFactorySpi.class);

	private KeyManagerFactory delegate;

	private Map<Principal, String> issuerAliases;

	private String delegatedAlgo = "SunX509";

	public DelegatedKeyManagerFactorySpi() throws NoSuchAlgorithmException {
		delegate = KeyManagerFactory.getInstance(delegatedAlgo);
		issuerAliases = new ConcurrentHashMap<>();
	}

	@Override
	protected KeyManager[] engineGetKeyManagers() {
		return new KeyManager[] { new PrincipalAliasMappingX509KeyManager(issuerAliases, Arrays.stream(delegate.getKeyManagers()).toArray(X509KeyManager[]::new)) };
	}

	@Override
	protected void engineInit(ManagerFactoryParameters spec) throws InvalidAlgorithmParameterException {
		delegate.init(spec);
	}

	@Override
	protected void engineInit(KeyStore ks, char[] password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
		try {
			delegate.init(ks, password);
		} catch (Exception e) {
			logger.error("error on initialize keystore, {}", e.getMessage());
			logger.debug("details :", e);
		}
		for (Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements();) {
			String alias = aliases.nextElement();
			try {
				X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
				if (cert != null) {
					issuerAliases.put(cert.getIssuerX500Principal(), alias);
					continue;
				}
			} catch (Exception e) {
				logger.error("error on engineInit building principal map, alias : [{}]", alias);
				logger.debug("details :", e);
			}
		}
	}
}
