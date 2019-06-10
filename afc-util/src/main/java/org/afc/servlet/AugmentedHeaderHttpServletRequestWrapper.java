package org.afc.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.afc.util.ListEnumeration;

public class AugmentedHeaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private static final ThreadLocal<SimpleDateFormat[]> formats = new ThreadLocal<SimpleDateFormat[]>() {
		
		@Override
		public SimpleDateFormat[] initialValue() {
			return new SimpleDateFormat[] {
		        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
		        new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
		        new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
			};
		}
	};
	
	private Map<String, List<String>> headers;

	public AugmentedHeaderHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.headers = new LinkedHashMap<>();
	}

	public void addHeader(String name, String value) {
		List<String> values = headers.get(name);
		if (values == null) {
			values = new LinkedList<>();
			headers.put(name, values);
		}
		values.add(value);
	}

	@Override
	public long getDateHeader(String name) {
		List<String> values = headers.get(name);
		if (values == null) {
			return super.getDateHeader(name);
		}

		String value = values.get(0);
		for (SimpleDateFormat formatter : formats.get()) {
			try {
				long d = formatter.parse(value).getTime();
				if (d > -1L) {
					return d;
				}
			} catch (ParseException e) {
			}
		}
		return -1L;
	}

	@Override
	public String getHeader(String name) {
		List<String> values = headers.get(name);
		if (values == null) {
			return super.getHeader(name);
		}
		return values.get(0);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		List<String> headerNames = new LinkedList<>();
		for (Enumeration<String> names = super.getHeaderNames(); names.hasMoreElements(); ) {
			headerNames.add(names.nextElement());
		}
		
		headerNames.addAll(headers.keySet());
		return new ListEnumeration<>(headerNames);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		List<String> values = headers.get(name);
		if (values == null) {
			return super.getHeaders(name);
		}
		return new ListEnumeration<>(values);
	}

	@Override
	public int getIntHeader(String name) {
		List<String> values = headers.get(name);
		if (values == null) {
			return super.getIntHeader(name);
		}
		return Integer.parseInt(values.get(0));
	}
}
