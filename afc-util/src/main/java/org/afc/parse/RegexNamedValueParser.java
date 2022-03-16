package org.afc.parse;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class RegexNamedValueParser implements NamedValueParser {

	private static final Pattern NAMED_PATTERN = Pattern.compile("\\(%.*?=.*?%\\)");

	private List<String> names;

	private Pattern compliedLinePattern;

	public RegexNamedValueParser(String linePattern) {
		this.names = new LinkedList<>();
		compile(linePattern);
	}

	@Override
	public Map<String, String> parse(CharSequence line) {
		Map<String, String> map = new LinkedHashMap<>();
		Matcher matcher = compliedLinePattern.matcher(line);
		if (matcher.find()) {
			for (int i = 0; i < names.size(); i++) {
				map.put(names.get(i), matcher.group(i + 1));
			}
		}
		return map;
	}

	private void compile(String linePattern) {
		for (Matcher matcher = NAMED_PATTERN.matcher(linePattern);
			 matcher.find();
			 matcher = NAMED_PATTERN.matcher(linePattern)) {

			String matchedName = matcher.group();
			String name = matchedName.substring(2, matchedName.indexOf('='));
			String value = matchedName.substring(matchedName.indexOf('=') + 1, matchedName.length() - 2);
			linePattern = matcher.replaceFirst('(' + value + ')');
			names.add(name);
		}
		compliedLinePattern = Pattern.compile(linePattern);
	}
}