package org.afc.parse;

import static org.afc.util.CollectionUtil.*;
import static org.afc.util.JUnitUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import org.afc.junit5.extension.TestInfoExtension;
import org.afc.util.JUnit4Util;
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

@ExtendWith({ TestInfoExtension.class })
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(Random.class)
class RegexNamedValueParserTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testParseString() {
		RegexNamedValueParser parser = new RegexNamedValueParser(".*?\\[(%thread=.*?%)\\]\\[.*?\\]\\[(%clazz=.*?%)\\].*");
		Map<String, String> actual = actual(parser.parse("2017-09-25 21:23:13.141 [main][INFO ][o.a.m.collector.NioFileCollector] last size : 58"));
		Map<String, String> expect = expect(map(new HashMap<>(),
			"thread", "main",
            "clazz", "o.a.m.collector.NioFileCollector"
		));

		assertMap("name and value", actual, expect);
	}

	@Test
	void testParseCharBuffer() {
		RegexNamedValueParser parser = new RegexNamedValueParser(".*?\\[(%thread=.*?%)\\]\\[.*?\\]\\[(%clazz=.*?%)\\].*");

		CharBuffer buffer = CharBuffer.allocate(256);
		buffer.append("2017-09-25 21:23:13.141 [main][INFO ][o.a.m.collector.NioFileCollector] last size : 58");
		buffer.position(0);

		Map<String, String> actual = actual(parser.parse(buffer));
		Map<String, String> expect = expect(map(new HashMap<>(),
			"thread", "main",
            "clazz", "o.a.m.collector.NioFileCollector"
		));
		assertMap("name and value", actual, expect);
	}
}
