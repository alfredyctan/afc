package org.afc.jsse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.util.Enumeration;

import org.afc.util.StringUtil;


public class KeyStoreTools {

	private static final int OPER = 0;

	private static final int KEYSTORE = 1;

	private static final int STOREPASS = 2;

	private static final int SRC_ALIAS = 3;

	private static final int TAR_ALIAS = 4;

	private static final int KEYPASS = 5;

	public static void main(String[] args) throws Exception {
		if ("rename".equals(args[OPER])) {
			rename(args);
		} else {
			System.out.println("unsupported operation " + args[OPER]);
		}
	}

	public static void rename(String[] args) throws Exception {
		KeyStore ks = KeyStore.getInstance("JKS");

		File file = new File(args[KEYSTORE]);
		char[] password = args[STOREPASS].toCharArray();
		
		char[] keypass = StringUtil.hasValue(args[KEYPASS]) ? args[KEYPASS].toCharArray() : password;
		
		InputStream is = new FileInputStream(file);
		ks.load(is, password);
		is.close();
		for (Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements();) {
			String alias = aliases.nextElement();
			if (alias.matches(args[SRC_ALIAS])) {
				System.out.println("alias [" + alias + "] matches " + args[SRC_ALIAS]);
				
				Entry entry = ks.getEntry(alias, new PasswordProtection(keypass));
				ks.deleteEntry(alias);
				ks.setEntry(args[TAR_ALIAS], entry, new PasswordProtection(keypass));
				
				OutputStream os = new FileOutputStream(file);
				ks.store(os, password);
				os.close();
			}
		}
	}
}
