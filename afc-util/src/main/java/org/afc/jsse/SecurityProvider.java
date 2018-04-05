package org.afc.jsse;

import java.security.Provider;
import java.security.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityProvider extends Provider {

	private static final Logger logger = LoggerFactory.getLogger(SecurityProvider.class);
	
	private static final long serialVersionUID = -923206889973876245L;
	
	public SecurityProvider() {
		super("AFC", 1.0D, "AFC Security Provider 1.0");

		put("KeyManagerFactory.AFC", DelegatedKeyManagerFactorySpi.class.getName());
		logger.info("Security Provider : KeyManagerFactory.AFC added with {}", DelegatedKeyManagerFactorySpi.class.getName());

		Security.insertProviderAt(this, 0);
		logger.info("Security : {} added to head", SecurityProvider.class.getName());

		Security.setProperty("ssl.KeyManagerFactory.algorithm", "AFC");
		logger.info("Security : ssl.KeyManagerFactory.algorithm set to AFC");

	}
}

