package org.afc.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.afc.json.everit.EveritJsonSchemaValidator;
import org.afc.util.FileUtil;
import org.afc.util.JUnitUtil;

public class EveritJsonSchemaValidatorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

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
	public void testNormal() throws Exception {
		EveritJsonSchemaValidator validator = new EveritJsonSchemaValidator("json/schema");
		validator.validate(FileUtil.readFileAsString("json/msg/success.json"), "initial.json#/Initial");
	}

	@Test
	public void testFailValidation() throws Exception {
		EveritJsonSchemaValidator validator = new EveritJsonSchemaValidator("json/schema/");
		try {
			validator.validate(FileUtil.readFileAsString("json/msg/fail-length.json"), "initial.json#/Initial");
			fail("should caught Exception");
		} catch (JsonValidationException e) {
			assertThat("exception message", e.getMessage(), is(equalTo("#/payoff/acquirer: expected maxLength: 5, actual: 6")));
		}
		try {
			validator.validate(FileUtil.readFileAsString("json/msg/fail-date.json"), "initial.json#/Initial");
			fail("should caught Exception");
		} catch (JsonValidationException e) {
			assertThat("exception message", e.getMessage(), is(equalTo("#/payoff/issueDate: [2018-11-a07] is not a valid date. Expected [yyyy-MM-dd]")));
		}
	}

	@Test
	public void testInvalidSchemaBase() throws Exception {
		EveritJsonSchemaValidator validator = new EveritJsonSchemaValidator("json/schema2");
		try {
			validator.validate(FileUtil.readFileAsString("json/msg/success.json"), "initial.json#/Initial");
		} catch (JsonValidationException e) {
			assertThat("exception message", e.getMessage(), is(equalTo("schema not found in [json/schema2/initial.json]")));
		}
	}

	@Test
	public void testInvalidSchemaRef() throws Exception {
		EveritJsonSchemaValidator validator = new EveritJsonSchemaValidator("json/schema");
		try {
			validator.validate(FileUtil.readFileAsString("json/msg/success.json"), "invalid-ref.json#/Initial");
		} catch (JsonValidationException e) {
			assertThat("exception message", e.getMessage(), is(equalTo("no protocol: json/schema/payoff-error.json")));
		}
	}

	@Test
	public void testInvalidSchemaRefPointer() throws Exception {
		EveritJsonSchemaValidator validator = new EveritJsonSchemaValidator("json/schema");
		try {
			validator.validate(FileUtil.readFileAsString("json/msg/success.json"), "invalid-ref-ptr.json#/Initial");
		} catch (JsonValidationException e) {
			assertThat("exception message", e.getMessage(), is(equalTo("#: key [PayoffNotExist] not found")));
		}
	}
}
