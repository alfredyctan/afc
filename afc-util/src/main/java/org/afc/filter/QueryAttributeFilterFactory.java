package org.afc.filter;

import java.util.LinkedList;
import java.util.List;

import org.afc.AFCException;
import org.afc.util.StringUtil;

import lombok.AllArgsConstructor;

public class QueryAttributeFilterFactory<T> implements AttributeFilterFactory<T, String> {
	
	private static final String OR = "||";

	private static final String AND = "&&";

	private char O;
	
	private char C;

	@AllArgsConstructor
	private static class Range {
		int start;
		int end;
	}

	private AttributeFilterFactory<T, String> delegate;
	
	public QueryAttributeFilterFactory(AttributeFilterFactory<T, String> delegate) {
		this(delegate, '(', ')');
	}

	public QueryAttributeFilterFactory(AttributeFilterFactory<T, String> delegate, char o, char c) {
		this.delegate = delegate;
		this.O = o;
		this.C = c;
	}

	@Override
	public AttributeFilter<T> create(String filter) {
		if (StringUtil.hasNoValue(filter)) {
			return new PassThruAttributeFilter<>();
		}
		List<AttributeFilter<T>> subFilters = new LinkedList<>(); // backlog for &&, || filters
		List<Range> anchor = new LinkedList<>(); // pos of expression range in filter string
		
		int start = -1;
		int end = -1;
		int open = 0;
		for (int i = 0; i < filter.length(); i++) {
			if (filter.charAt(i) == O) { // lookup open blanket
				if (start == -1) {
					start = i;
				}
				open++;
			}
			if (filter.charAt(i) == C) { // lookup close blanket
				open--;
				end = i;
			}
			if (open == 0 && start > -1 && end > -1) {	// create sub-filter for fully closed blanket		
				subFilters.add(create(filter.substring(start + 1, end))); // anchor the pos for error message
				anchor.add(new Range(start, end + 1)); //
				start = -1;
				end = -1;
				open = 0;
			}
		}
		if (subFilters.size() > 0) { // found some sub-filter and wrap with AND/OR
			
			// make sure consistency of and or condition within same level
			String orAnd = null;
			int bound = anchor.size() - 1;
			for (int i = 0; i < bound; i++) {
				String type = filter.substring(anchor.get(i).end, anchor.get(i + 1).start).trim();
				orAnd = (orAnd != null) ? orAnd : type;
				if (!type.equals(orAnd)) {
					throw new AFCException('[' + filter.substring(anchor.get(i).end, anchor.get(i + 1).start) + "] is invalid, pos : " + anchor.get(i).end + " - " +  anchor.get(i + 1).start);
				}
			}
			
			if (AND.equals(orAnd)) {
				return new AndAttributeFilter<>(subFilters);
			} else if (OR.equals(orAnd)) {
				return new OrAttributeFilter<>(subFilters);
			} else {
				throw new AFCException("syntax error: [" + filter.substring(anchor.get(anchor.size()-1).end) + ']');
			}
		} else {
			return delegate.create(filter);
		}
	}
}
