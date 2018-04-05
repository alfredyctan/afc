package org.afc.jsse;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisableValidationTrustManager implements X509TrustManager {

	private static final Logger logger = LoggerFactory.getLogger(DisableValidationTrustManager.class);
	
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		if (logger.isDebugEnabled()) {
			logger.debug("always trusted client. cert chain:[{}], authType:[{}]", Arrays.asList(chain), authType);
		}
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		if (logger.isDebugEnabled()) {
			logger.debug("always trusted server. cert chain:[{}], authType:[{}]", Arrays.asList(chain), authType);
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}