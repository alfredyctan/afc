package org.afc.resolve;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.afc.AFCException;

public class Expression {
	
	public static Resolver resolver() {
		return new DefaultExpressionResolver();
	}

	public static Resolver resolver(String open, String close, int rptWidth) {
		return new DefaultExpressionResolver(open, close, rptWidth);
	}
	
	public static interface Resolver {
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
	}
}
