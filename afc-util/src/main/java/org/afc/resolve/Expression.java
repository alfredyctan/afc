package org.afc.resolve;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.afc.AFCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is thread-safe string resolver util to resolve string expression recursively,
 * all modifier should be implemented in stateless or thread-safe manner
 *
 * eg.
 * String resolved = Expression.resolver().resolve("env user:%{$env(USERNAME)}", new HashMap<>()));
 */
public class Expression {

	public static Resolver resolver() {
		return new DefaultExpressionResolver();
	}

	public static Resolver resolver(String open, String close, int rptWidth) {
		return new DefaultExpressionResolver(open, close, rptWidth);
	}

	public static interface Resolver {

		Function<StringBuilder, StringBuilder> getDelegate(Map<? extends CharSequence, ? extends CharSequence> source);

		public default String resolve(String str, Map<? extends CharSequence, ? extends CharSequence> source) {
			return resolve(str, getDelegate(source));
		}

		public String resolve(String str, Function<StringBuilder, StringBuilder> processor);
	}

	private static class DefaultExpressionResolver implements Resolver {

		private static final Logger logger = LoggerFactory.getLogger(Expression.class);

		private final int RPT_WIDTH;

		private final String OPEN;

		private final int OPEN_LEN;

		private final String CLOSE;

		private final int CLOSE_LEN;

		private final int OPEN_CLOSE_LEN;

		private Modifier modifier;

		public DefaultExpressionResolver() {
			this("%{", "}", 20);
		}

		public DefaultExpressionResolver(String open, String close, int rptWidth) {
			this.RPT_WIDTH = rptWidth;
			this.OPEN = open;
			this.OPEN_LEN = open.length();
			this.CLOSE = close;
			this.CLOSE_LEN = close.length();
			this.OPEN_CLOSE_LEN = OPEN_LEN + CLOSE_LEN;
			this.modifier = new DefaultExpressionModifier();
		}

		@Override
		public String resolve(String str, Function<StringBuilder, StringBuilder> processor) {
			return resolve(new StringBuilder(str), processor, false).toString();
		}

		private StringBuilder resolve(StringBuilder str, Function<StringBuilder, StringBuilder> processor, boolean process) {
			int start = -1;
			int end = -1;
			int open = 0;
			int pOpen = 0;
			int pClose = -1;
			while ((pOpen = str.indexOf(OPEN, pOpen)) > -1) {
				// logger.info("open loop, start:[{}], end:[{}], open:[{}], pOpen:[{}],
				// pClose:[{}]", start, end, open, pOpen, pClose);
				if (start == -1) {
					start = pOpen;
				}
				open++;

				pClose = pOpen;
				pOpen = str.indexOf(OPEN, pOpen + OPEN_LEN);
				while ((pClose = str.indexOf(CLOSE, pClose + 1)) > -1) {
					// logger.info("close loop, start:[{}], end:[{}], open:[{}], pOpen:[{}],
					// pClose:[{}]", start, end, open, pOpen, pClose);
					if (pOpen == -1 || pClose < pOpen) {
						// found close
						open--;
						end = pClose;
						break;
					}
				}
				if (pClose == -1 && open != 0) {
					throw new AFCException("incomplete syntax, pos:" + start + ", [... " + str.substring(start - RPT_WIDTH < 0 ? 0 : start - RPT_WIDTH, start + RPT_WIDTH) + " ...]");
				}
				if (open == 0 && start > -1 && end > -1) {
					String subStr = str.substring(start + OPEN_LEN, end);
					StringBuilder resolved = resolve(new StringBuilder(subStr), processor, true);
					str.replace(start, end + CLOSE_LEN, resolved.toString());
					pOpen = pOpen - subStr.length() - OPEN_CLOSE_LEN + resolved.length();
					start = -1;
					end = -1;
				}
			}

			return (process) ? processor.apply(str) : str;
		}

		@Override
		public Function<StringBuilder, StringBuilder> getDelegate(Map<? extends CharSequence, ? extends CharSequence> source) {
			return ss -> {
				return modifier.modify(ss, source, null);
			};
		}
	}

	/**
	 * all sub-class implemented this interface, need to be stateless or thread-safe
	 * @author atyc
	 */
	public interface Modifier {

		public Pattern pattern();

		public StringBuilder modify(StringBuilder str, Map<? extends CharSequence, ? extends CharSequence> source, Matcher matcher);

		default StringBuilder replace(StringBuilder str, String value) {
			return (value != null) ? str.replace(0, str.length(), value) : str;
		}
	}

	private static class DefaultExpressionModifier implements Modifier {

		private List<Modifier> modifiers;

		public DefaultExpressionModifier() {
			this.modifiers = List.of(
				new TagModifier(),
				new SysPropModifier(),
				new EnvModifier()
			);
		}

		@Override
		public Pattern pattern() {
			return null;
		}

		@Override
		public StringBuilder modify(StringBuilder str, Map<? extends CharSequence, ? extends CharSequence> source, Matcher matcher) {
			for (Modifier modifier : modifiers) {
				Matcher modMatcher = modifier.pattern().matcher(str);
				if (modMatcher.matches()) {
					return modifier.modify(str, source, modMatcher);
				}
			}
			return str;
		}
	}

	private static class EnvModifier implements Modifier {

		private static final Pattern ENV = Pattern.compile("^\\s*?\\$env\\s*?\\(\\s*?(?<key>\\S*?)\\s*?\\)\\s*?$");

		@Override
		public Pattern pattern() {
			return ENV;
		}

		@Override
		public StringBuilder modify(StringBuilder str, Map<? extends CharSequence, ? extends CharSequence> source, Matcher matcher) {
			try {
				String value = System.getenv(matcher.group("key"));
				value = (value == null) ? "" : value;
				return replace(str, value);
			} catch(Exception e) {
				return str;
			}
		}
	}

	private static class SysPropModifier implements Modifier {

		private static final Pattern SYS = Pattern.compile("^\\s*?\\$sys\\s*?\\(\\s*?(?<key>\\S*?)\\s*?\\)\\s*?$");

		@Override
		public Pattern pattern() {
			return SYS;
		}

		@Override
		public StringBuilder modify(StringBuilder str, Map<? extends CharSequence, ? extends CharSequence> source, Matcher matcher) {
			try {
				String value = System.getProperty(matcher.group("key"));
				value = (value == null) ? "" : value;
				return replace(str, value);
			} catch(Exception e) {
				return str;
			}
		}
	}

	private static class TagModifier implements Modifier {

		private static final Pattern FIELD = Pattern.compile("^\\s*?\\#(?<tag>\\S*?)\\s*?(,\\s*?(?<alt>#\\S*?))?$");

		@Override
		public Pattern pattern() {
			return FIELD;
		}

		@SuppressWarnings({"squid:S6019"})
		@Override
		public StringBuilder modify(StringBuilder str, Map<? extends CharSequence, ? extends CharSequence> source, Matcher matcher) {
			try {
				CharSequence value = source.get(matcher.group("tag"));
				if (value != null) {
					return replace(str, value.toString());
				} else {
					String[] alts = matcher.group("alt").split("\\s*?,\\s*?");
					for (int i = 0; i < alts.length; i++) {
						value = source.get(alts[i].substring(1));
						if (value != null) {
							return replace(str, value.toString());
						}
					}
					return replace(str, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return str;
			}
		}
	}

}
