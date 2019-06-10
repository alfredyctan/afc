package org.afc.querydsl;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.afc.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
/**
 * 
 * Sequential Structure:
 * 
 * BooleanExpression predicate = expr(QTrade.trade.id::eq, (Long)null);
 * predicate = and(predicate, expr(QTrade.trade.product::eq, ProductType.fromAbbr(null))); 
 * predicate = and(predicate, expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr(null)));
 * 
 * Nested Structure:
 * 
 * BooleanExpression predicate = expr(QTrade.trade.id::eq, 100L);
 * predicate = and(
 *     predicate, 
 *     or(
 *         expr(QTrade.trade.product::eq, ProductType.fromAbbr("ELN")), 
 *         expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr("AL"))
 *     )
 * );
 * 
 * @author atyc
 *
 */
public class QueryDSLUtil {

	public static <T> BooleanExpression expr(Function<T, BooleanExpression> expr, T value) {
		return (value == null || StringUtil.hasNoValue(value.toString())) ? null : expr.apply(value);
	}

	public static <T, U> BooleanExpression expr(BiFunction<T, U, BooleanExpression> expr, T value1, U value2) {
		return (value1 == null || StringUtil.hasNoValue(value1.toString()) || value2 == null || StringUtil.hasNoValue(value2.toString())) ? null : expr.apply(value1, value2);
	}
	
	public static <T> BooleanExpression or(BooleanExpression left, BooleanExpression right) {
		if (left != null && right != null) {
			return expr(left::or, right);
		} else if (left != null) {
			return left;
		} else if (right != null) {
			return right;
		} else {
			return null;
		}
	}
	
	public static <T> BooleanExpression and(BooleanExpression left, BooleanExpression right) {
		if (left != null && right != null) {
			return expr(left::and, right);
		} else if (left != null) {
			return left;
		} else if (right != null) {
			return right;
		} else {
			return null;
		}
	}
}
