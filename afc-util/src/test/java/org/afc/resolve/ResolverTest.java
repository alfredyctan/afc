package org.afc.resolve;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.afc.resolve.Expression.Resolver;
import org.afc.util.JUnitUtil;


public class ResolverTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		Resolver resolver1 = Expression.resolver("${", "}", 20);
		
		String resolved = resolver1.resolve("${sys.log}/${sys.service}/${sys.service}.${sys.cluster}.${sys.instance}.%{d:yyyy-MM-dd}.log.%{i}", src -> {
			src.reverse();
			return src;
		});
		
		Resolver resolver2 =  Expression.resolver("%{", "}", 20);
		resolved = resolver2.resolve(resolved, src -> {
			src.reverse();
			return src;
		});
		
		String actual = JUnitUtil.actual(resolved);
		String expect = JUnitUtil.expect("gol.sys/ecivres.sys/ecivres.sys.retsulc.sys.ecnatsni.sys.dd-MM-yyyy:d.log.i");
		
		assertThat("", actual, is(equalTo(expect)));
	}

	@Test
	public void testUnclose() throws Exception {
		Resolver resolver1 = Expression.resolver("${", "}", 20);
		
		try {
			resolver1.resolve("${sys.log/${sys.service}/${sys.service}.${sys.cluster}.${sys.instance}.%{d:yyyy-MM-dd}.log.%{i}", src -> {
				return src;
			});
			fail("should throw error");
		} catch (Exception e) {
			String actual = JUnitUtil.actual(e.getMessage());
			String expect = JUnitUtil.expect("incomplete syntax, pos:0, [... ${sys.log/${sys.serv ...]");
			assertThat("", actual, is(equalTo(expect)));
		}
	}
}
