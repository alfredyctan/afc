package org.afc.jsse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.util.Enumeration;

import org.junit.Test;

public class KeyStoreToolsTest {

	@Test
	public void testRename() {
		try {
			KeyStoreTools.main(new String[] { "rename", "target/test-classes/ugly-keystore.jks", "localhost", "70d3e1a9.*", "goodname", "abcd1234" });
			
			KeyStore ks = KeyStore.getInstance("JKS");

			File file = new File("target/test-classes/ugly-keystore.jks");
			char[] password = "localhost".toCharArray();
			char[] keypass = "abcd1234".toCharArray();
			
			InputStream is = new FileInputStream(file);
			ks.load(is, password);
			is.close();
			for (Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements();) {
				String alias = aliases.nextElement();

				if (alias.equals("goodname")) {
					Entry entry = ks.getEntry(alias, new PasswordProtection(keypass));
				}
			}
			System.out.println("DONE Success");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
