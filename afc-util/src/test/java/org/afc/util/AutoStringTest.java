package org.afc.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import lombok.AllArgsConstructor;

public class AutoStringTest {

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
	public void test() {
		Family family = new Family(
			3,
			"Chan Family", 
			new Parent(50, "Chan"), 
			ListUtil.list(new ArrayList(), new Brother(25, "Chan", "big"), new Brother(22, "Chan", "small")),
			Arrays.asList(new Sister(12, "Chan", "bigs"), new Sister(10, "Chan", "smalls")).toArray(new Sister[0]),
			new MapUtil().map(new HashMap<>(), "USD", new BigDecimal("0.0"), "HKD", new BigDecimal("1000.0"))
		);
		
		String actual = JUnitUtil.actual(family.toString());
		String expect = JUnitUtil.expect("Family(size=3, familyName=Chan Family, parent=Parent(age=50, lastName=Chan), brothers=[Brother(firstName=big, age=25, lastName=Chan), Brother(firstName=small, age=22, lastName=Chan)], sisters=[Sister(firstName=bigs, age=12, lastName=Chan), Sister(firstName=smalls, age=10, lastName=Chan)], cashs={HKD=1000.0, USD=0.0})");
		
		assertThat("same", actual, is(equalTo(expect)));
	}


	@AllArgsConstructor
	static class Family {

		Integer size;
		
		String familyName;

		Parent parent;

		List<Brother> brothers;

		Sister[] sisters;

		Map<String, BigDecimal> cashs;
		
		@Override
		public String toString() {
			return AutoString.of(this);
		}
	}
	
	@AllArgsConstructor
	static class Parent {

		int age;
		
		String lastName;
		
		@Override
		public String toString() {
			return AutoString.of(this);
		}

	}

	static class Brother extends Parent {

		String secret;
		
		String firstName;

		public Brother(int age, String lastName, String firstName) {
			super(age, lastName);
			this.firstName = firstName;
		}

		@Override
		public String toString() {
			return AutoString.of(this);
		}
	}

	static class Sister extends Parent {

		String firstName;

		@AutoString.Hide
		int realAge;

		public Sister(int age, String lastName, String firstName) {
			super(age, lastName);
			this.firstName = firstName;
			this.realAge = age + 1;
		}

		@Override
		public String toString() {
			return AutoString.of(this);
		}
	}
}
