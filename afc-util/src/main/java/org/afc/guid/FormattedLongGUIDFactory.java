package org.afc.guid;

import org.afc.util.StringUtil;

public class FormattedLongGUIDFactory implements GUIDFactory<String> {

	private GUIDFactory<Long> delegate;
	
	private final int radix;

	private String prefix;
	
	private String padding;
	
	private int length;

	public FormattedLongGUIDFactory(GUIDFactory<Long> delegate, int radix, String prefix, String paddingChar, int length) {
		super();
		this.delegate = delegate;
		this.radix = radix;
		this.prefix = prefix;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < length; i++) {
			buffer.append(paddingChar);
		}
		this.padding = buffer.substring(0, length - prefix.length());
		this.length = length - prefix.length();
	}

	@Override
	public String generate() {
		String id = StringUtil.right(padding + Long.toString(delegate.generate(), radix).toUpperCase(), length);
		return (prefix != null) ? prefix + id : id;
	}
}
