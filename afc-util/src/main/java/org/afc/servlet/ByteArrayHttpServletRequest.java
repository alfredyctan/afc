package org.afc.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ByteArrayHttpServletRequest extends HttpServletRequestWrapper {

	private byte[] content;

	public ByteArrayHttpServletRequest(HttpServletRequest request, byte[] content) {
		super(request);
		this.content = content;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ByteArrayServletInputStream(content);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	public static class ByteArrayServletInputStream extends ServletInputStream {

		private ByteArrayInputStream buffer;

		public ByteArrayServletInputStream(byte[] content) {
			this.buffer = new ByteArrayInputStream(content);
		}

		@Override
		public int read() throws IOException {
			return buffer.read();
		}

		@Override
		public boolean isFinished() {
			return buffer.available() == 0;
		}

		@Override
		public boolean isReady() {
			return buffer.available() > 0;
		}

		@Override
		public void setReadListener(ReadListener listener) {

		}
	}
}