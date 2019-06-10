package org.afc.spring.ssl;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientConnectionManagerFactory;

import org.afc.jsse.SSLContextFactory;

public class MutualHandshakeApacheHttpClientConnectionManagerFactory extends DefaultApacheHttpClientConnectionManagerFactory {

	private static final Logger logger = LoggerFactory.getLogger(MutualHandshakeApacheHttpClientConnectionManagerFactory.class);
	
	@Override
	public HttpClientConnectionManager newConnectionManager( boolean disableSslValidation, int maxTotalConnections, int maxConnectionsPerRoute, long timeToLive, TimeUnit timeUnit, RegistryBuilder registryBuilder) {
		logger.info("new connection manager - disableSslValidation:[{}], maxTotalConnections:[{}], maxConnectionsPerRoute:[{}], timeToLive:[{}]", disableSslValidation, maxTotalConnections, maxConnectionsPerRoute, timeToLive);
		if (registryBuilder == null) {
			registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create().register(HTTP_SCHEME, PlainConnectionSocketFactory.INSTANCE);
		}
		if (disableSslValidation) {
			SSLContext sslContext = SSLContextFactory.createWithDisableTrustManager(System.getProperties());
			registryBuilder.register(HTTPS_SCHEME, new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE));
		} else {
			registryBuilder.register("https", new SSLConnectionSocketFactory(SSLContexts.createSystemDefault()));
		}
		final Registry<ConnectionSocketFactory> registry = registryBuilder.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry, null, null, null, timeToLive, timeUnit);
		connectionManager.setMaxTotal(maxTotalConnections);
		connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

		return connectionManager;
	}
}
