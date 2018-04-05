package org.afc.jsse;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.X509KeyManager;

public class PrincipalAliasMappingX509KeyManager implements X509KeyManager {

	private X509KeyManager[] delegates;
	
	private Map<Principal, String> principalAliases;

	public PrincipalAliasMappingX509KeyManager(Map<Principal, String> principalAliases, X509KeyManager[] delegates) {
		this.principalAliases = principalAliases;
		this.delegates = delegates;
	}

	@Override
	public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
		String alias = principalAliases.get(issuers[0]);
		if (alias != null) {
			return alias;
		}
		
		for (X509KeyManager delegate : delegates) {
			String result = delegate.chooseClientAlias(keyTypes, issuers, socket);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
		if (delegates == null) {
			return null;
		}
			
		for (X509KeyManager delegate : delegates) {
			String result = delegate.chooseServerAlias(keyType, issuers, socket);
			if (result != null) {
				return result;
			}
		}
		return null;
	}


	@Override
	public X509Certificate[] getCertificateChain(String alias) {
		if (delegates == null) {
			return null;
		}
		for (X509KeyManager delegate : delegates) {
			X509Certificate[] result = delegate.getCertificateChain(alias);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public String[] getClientAliases(String keyType, Principal[] issuers) {
		if (delegates == null) {
			return null;
		}
		for (X509KeyManager delegate : delegates) {
			String[] result = delegate.getClientAliases(keyType, issuers);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public PrivateKey getPrivateKey(String alias) {
		if (delegates == null) {
			return null;
		}
		for (X509KeyManager delegate : delegates) {
			PrivateKey result = delegate.getPrivateKey(alias);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public String[] getServerAliases(String keyType, Principal[] issuers) {
		if (delegates == null) {
			return null;
		}
		for (X509KeyManager delegate : delegates) {
			String[] result = delegate.getServerAliases(keyType, issuers);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}
