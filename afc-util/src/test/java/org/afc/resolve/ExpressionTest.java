package org.afc.resolve;

import static org.afc.util.JUnitUtil.*;
//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
//import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.afc.junit5.extension.TestInfoExtension;
import org.afc.resolve.Expression.Resolver;
import org.afc.util.JUnitUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith({ TestInfoExtension.class })
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(Random.class)
public class ExpressionTest {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	void testResolved() throws Exception {
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

		String actual = actual(resolved);
		String expect = expect("gol.sys/ecivres.sys/ecivres.sys.retsulc.sys.ecnatsni.sys.dd-MM-yyyy:d.log.i");

		assertEquals("resolved with different blanket", actual, expect);
	}

	@Test
	void testUnclose() throws Exception {
		Resolver resolver1 = Expression.resolver("${", "}", 20);

		try {
			resolver1.resolve("${sys.log/${sys.service}/${sys.service}.${sys.cluster}.${sys.instance}.%{d:yyyy-MM-dd}.log.%{i}", src -> {
				return src;
			});
			fail("should throw error");
		} catch (Exception e) {
			String actual = JUnitUtil.actual(e.getMessage());
			String expect = JUnitUtil.expect("incomplete syntax, pos:0, [... ${sys.log/${sys.serv ...]");
			assertEquals("error reported", actual, expect);
		}
	}

	@ParameterizedTest(name = "{index}-Resolve-{0}")
	@MethodSource("testResolveCases")
	void testResolve(String name, String in, String out) {
		System.setProperty("username", "peter");
		HashMap<CharSequence, CharSequence> source = new HashMap<>();
		source.put("logger", "TestLogger");
		source.put("ctx", "ABCD1234");

		Resolver resolver1 = Expression.resolver();
		String actual = actual(resolver1.resolve(in, source));
		String expect = expect(out);

		assertEquals(name, actual, expect);
	}

	private Stream<Arguments> testResolveCases() {
		return Stream.of(
			Arguments.of("SysProp", "env user:%{$sys(username)}", "env user:peter"),
			Arguments.of("tag found 1",     "tag:%{#logger}",            "tag:TestLogger"),
			Arguments.of("tag found 2nd",   "tag:%{#notfound, #logger}",  "tag:TestLogger"),
			Arguments.of("tag skip 2nd",    "tag:%{#logger, #ctx}",       "tag:TestLogger")
		);
	}

	@Test
	void testEnv() throws Exception {
		Resolver resolver1 = Expression.resolver();
		String actual = actual(resolver1.resolve("env user:%{$env(USERNAME)}", new HashMap<>()));
		String expect = expect("env user:%{$env(USERNAME)}");
		assertNotEquals("expect name change", actual, expect);
	}
}
