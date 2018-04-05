package org.afc.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

public class ExceptionUtilTest {

	@Test
	public void testReturnIfCaughtException() {
		String caught = returnIfCaughtIOException(new FileNotFoundException("file not found"));
		assertThat(caught, is("file not found"));
	}

	@Test(expected=RuntimeException.class)
	public void testRethrowIfCaughtException() {
		String caught = returnIfCaughtIOException(new SQLException("sql error"));
	}

	@Test(expected=FileNotFoundException.class)
	public void testUnwrapException() throws Exception {
		Exception source = new Exception(new RuntimeException(new IOException(new FileNotFoundException("root"))));
		Exception root = ExceptionUtil.unwrap(source);
		throw root;
	}
	
	
	private static String returnIfCaughtIOException(Exception e) {
		try {
			throw e;
		} catch (Exception ex) {
			return ExceptionUtil.catching(
				e, 
				IOException.class, 
				t -> new String(t.getMessage())
			);
		}
	}
}
