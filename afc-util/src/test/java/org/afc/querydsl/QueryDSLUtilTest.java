package org.afc.querydsl;

import static org.afc.querydsl.QueryDSLUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.util.JUnitUtil;
import com.querydsl.core.types.dsl.BooleanExpression;

public class QueryDSLUtilTest {

	@Rule
	public TestName name = new TestName();

	@Before
	public void setUp() throws Exception {
		JUnitUtil.startCurrentTest(getClass(), name);
	}

	@After
	public void tearDown() throws Exception {
		JUnitUtil.endCurrentTest(getClass(), name);
	}

	@Test
	public void testSequentialAnd() {
		BooleanExpression predicate = expr(QTrade.trade.id::eq, (Long)null);
		predicate = and(predicate, expr(QTrade.trade.product::eq, ProductType.fromAbbr(null))); 
		predicate = and(predicate, expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr(null)));

		String actual = JUnitUtil.actual(null);
		String expect = JUnitUtil.expect(null);
		
		assertThat("sequential and", actual, is(equalTo(expect)));
	}
	
	@Test
	public void testSequentialAnd123() {
		BooleanExpression predicate = expr(QTrade.trade.id::eq, 100L);
		predicate = and(predicate, expr(QTrade.trade.product::eq, ProductType.fromAbbr("ELN"))); 
		predicate = and(predicate, expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr("AL")));

		String actual = JUnitUtil.actual(predicate.toString());
		String expect = JUnitUtil.expect("trade.id = 100 && trade.product = ELN && trade.status = ALLOCATED");
		
		assertThat("sequential and", actual, is(equalTo(expect)));
	}

	@Test
	public void testSequentialAnd1() {
		BooleanExpression predicate = expr(QTrade.trade.id::eq, 100L);
		predicate = and(predicate, expr(QTrade.trade.product::eq, ProductType.fromAbbr(null))); 
		predicate = and(predicate, expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr(null)));

		String actual = JUnitUtil.actual(predicate.toString());
		String expect = JUnitUtil.expect("trade.id = 100");
		
		assertThat("sequential and", actual, is(equalTo(expect)));
	}

	@Test
	public void testSequentialAnd3() {
		BooleanExpression predicate = expr(QTrade.trade.id::eq, (Long)null);
		predicate = and(predicate, expr(QTrade.trade.product::eq, ProductType.fromAbbr(null))); 
		predicate = and(predicate, expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr("AL")));

		String actual = JUnitUtil.actual(predicate.toString());
		String expect = JUnitUtil.expect("trade.status = ALLOCATED");
		
		assertThat("sequential and", actual, is(equalTo(expect)));
	}
	
	@Test
	public void testSequentialAnd12() {
		BooleanExpression predicate = expr(QTrade.trade.id::eq, 100L);
		predicate = and(predicate, expr(QTrade.trade.product::eq, ProductType.fromAbbr("ELN"))); 
		predicate = and(predicate, expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr(null)));

		String actual = JUnitUtil.actual(predicate.toString());
		String expect = JUnitUtil.expect("trade.id = 100 && trade.product = ELN");
		
		assertThat("sequential and", actual, is(equalTo(expect)));
	}
	
	@Test
	public void testSequentialAnd23() {
		BooleanExpression predicate = expr(QTrade.trade.id::eq, (Long)null);
		predicate = and(predicate, expr(QTrade.trade.product::eq, ProductType.fromAbbr("ELN"))); 
		predicate = and(predicate, expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr("AL")));

		String actual = JUnitUtil.actual(predicate.toString());
		String expect = JUnitUtil.expect("trade.product = ELN && trade.status = ALLOCATED");
		
		assertThat("sequential and", actual, is(equalTo(expect)));
	}
		
	@Test
	public void testNestedAndOr() {
		BooleanExpression predicate = expr(QTrade.trade.id::eq, 100L);
		predicate = and(
			predicate, 
			or(
				expr(QTrade.trade.product::eq, ProductType.fromAbbr("ELN")), 
				expr(QTrade.trade.status::eq, TradeStatusType.fromAbbr("AL"))
			)
		);
		String actual = JUnitUtil.actual(predicate.toString());
		String expect = JUnitUtil.expect("trade.id = 100 && (trade.product = ELN || trade.status = ALLOCATED)");
		
		assertThat("sequential and", actual, is(equalTo(expect)));
	}

}