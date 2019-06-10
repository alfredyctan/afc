package org.afc.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.AFCException;


public class ExpressionAttributeFilterFactory<T> implements AttributeFilterFactory<T, String> {

	private static final Logger logger = LoggerFactory.getLogger(ExpressionAttributeFilterFactory.class);
	
	private static final String OPERATOR = "(?<oper>\\s?!=\\s?|\\s?=\\s?|\\s?\\>=\\s?|\\s?\\>\\s?|\\s?\\<=\\s?|\\s?\\<\\s?|\\s!in\\s|\\sin\\s|\\s!like\\s|\\slike\\s|\\sregex\\s)";
	
	private static final Pattern PATTERN = Pattern.compile("(?<name>.*?)" + OPERATOR + "(?<attr>.*)$");
	
	private AttributeAccessor<T> accessor; 

	public ExpressionAttributeFilterFactory(AttributeAccessor<T> accessor) {
		this.accessor = accessor;
	}
	
	@Override
	public AttributeFilter<T> create(String filter) {
		Matcher matcher = PATTERN.matcher(filter);
		if (!matcher.matches()) {
			throw new AFCException("invalid filter (" + filter + ")");
		}
		
		String name = matcher.group("name");
		String oper = matcher.group("oper").trim();
		String attr = matcher.group("attr");
		logger.debug("filter : [{}] [{}] [{}]", name, oper, attr);
		switch (oper) {
			case "=":
				return new EqualAttributeFilter<>(name, attr, accessor);
			case "!=":
				return new NotAttributeFilter<>(new EqualAttributeFilter<>(name, attr, accessor));
			case ">":
				return new GreaterAttributeFilter<>(name, attr, accessor);
			case ">=":
				return new GreaterEqualAttributeFilter<>(name, attr, accessor);
			case "<":
				return new LesserAttributeFilter<>(name, attr, accessor);
			case "<=":
				return new LesserEqualAttributeFilter<>(name, attr, accessor);
			case "like":
				return new LikeAttributeFilter<>(name, attr, accessor::getString);
			case "!like":
				return new NotAttributeFilter<>(new LikeAttributeFilter<>(name, attr, accessor::getString));
			case "in":
				return new InAttributeFilter<>(name, attr, accessor::getString);
			case "!in":
				return new NotAttributeFilter<>(new InAttributeFilter<>(name, attr, accessor::getString));
			case "regex":
				return new RegexAttributeFilter<>(name, attr, accessor::getString);
			default:
				throw new AFCException("unsupported operator " + oper);
		}
	}
}
