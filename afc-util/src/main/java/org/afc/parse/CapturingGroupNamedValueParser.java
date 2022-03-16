package org.afc.parse;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapturingGroupNamedValueParser implements NamedValueParser {

	private static final Pattern NAMED_PATTERN = Pattern.compile("\\(\\?\\<.*?\\>.*?\\)");

	private List<String> names;

	private Pattern compliedLinePattern;

	public CapturingGroupNamedValueParser(String linePattern) {
		this.names = new LinkedList<>();
		compile(linePattern);
	}

	@Override
	public Map<String, String> parse(CharSequence line) {
		Map<String, String> map = new LinkedHashMap<>();
		Matcher matcher = compliedLinePattern.matcher(line);
		if (matcher.find()) {
			names.stream().forEach(name -> {
				map.put(name, matcher.group(name));
			});
		}
		return map;
	}

	private void compile(String linePattern) {
		Matcher matcher = NAMED_PATTERN.matcher(linePattern);
		while (matcher.find()) {
			String matchedName = matcher.group();
			String name = matchedName.substring(3, matchedName.indexOf('>'));
			names.add(name);
		}
		compliedLinePattern = Pattern.compile(linePattern);
	}

	@Override
	public String toString() {
		return compliedLinePattern.toString();
	}
}