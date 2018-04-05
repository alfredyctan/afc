package org.afc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.afc.util.ObjectComparator.Member;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ObjectComparatorTest {



	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	    ObjectComparator.setSilent(false);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompareEqual() {
	    JUnit4Util.startCurrentTest(this.getClass());
		
	    
		assertTrue(newObjectA(1).equals(newObjectA(1)));
		assertTrue(newObjectB(1).equals(newObjectB(1)));
		
		JUnit4Util.endCurrentTest(this.getClass());
	}

	
	@Test
	public void testCompareNotEqualObjectA() {
	    JUnit4Util.startCurrentTest(this.getClass());
		
	    try {
	    	assertFalse(newObjectA(1).equals(newObjectA(2)));
	    	fail("Should throw ObjectUnequalException");
	    } catch (ObjectUnequalException e) {
	    	assertEquals(ObjectA.class.getName() + ".priBoolean is not equal. false != true",e.getMessage());
	    }
		
		JUnit4Util.endCurrentTest(this.getClass());
	}

	@Test
	public void testCompareNotEqualObjectB() {
	    JUnit4Util.startCurrentTest(this.getClass());
		try {
	    	assertFalse(newObjectB(1).equals(newObjectB(2)));
	    	fail("Should throw ObjectUnequalException");
	    } catch (ObjectUnequalException e) {
	    	assertEquals(ObjectB.class.getName() + ".priBoolean is not equal. false != true",e.getMessage());
	    }
		JUnit4Util.endCurrentTest(this.getClass());
	}

	@Test
	public void testCompareNotEqualDifferentClass() {
	    JUnit4Util.startCurrentTest(this.getClass());
		
		assertFalse(newObjectA(1).equals(newObjectB(1)));
		
		JUnit4Util.endCurrentTest(this.getClass());
	}

	@Test
	public void testCompareNotEqualNull() {
	    JUnit4Util.startCurrentTest(this.getClass());
		
		assertFalse(newObjectB(1).equals(null));
		
		JUnit4Util.endCurrentTest(this.getClass());
	}

	@Test
	public void testCompareNotEqualSilent() {
	    JUnit4Util.startCurrentTest(this.getClass());
		
	    try {
		    ObjectComparator.setSilent(true);
			assertFalse(newObjectA(1).equals(newObjectA(2)));
			assertFalse(newObjectB(1).equals(newObjectB(2)));
			assertFalse(newObjectA(1).equals(newObjectB(1)));
			assertFalse(newObjectB(1).equals(null));
	    } catch (ObjectUnequalException oue) {
	    	oue.printStackTrace();
	    	fail("Should not throw ObjectUnequalException");
	    }
		JUnit4Util.endCurrentTest(this.getClass());
	}
		
	public ObjectA newObjectA(int seed) {

		ObjectA obj = new ObjectA(
			((seed % 2)==0),
			(byte)(seed * 10),
			String.valueOf(seed).charAt(0),
			(short)(seed * 20),
			(int)(seed * 30),
			(long)(seed * 40),
			(float)(seed * 1.1),
			(double)(seed * 2.1),
			String.valueOf(seed),       
			new Boolean(((seed % 2)==0)),
			new Byte((byte)(seed * 10)),
			new Character(String.valueOf(seed).charAt(0)),
			new Short((short)(seed * 20)),
			new Integer(seed * 30),
			new Long(seed * 40),
			new Float((float)seed * 1.1),
			new Double((double)seed * 2.1),
			new BigDecimal((double)seed * 1.99999999999999),
			new Date(seed * 99999999),         
			null
		);

		obj.add(newObjectB(10));
		obj.add(newObjectB(20));
		
		return obj;
	}

	public ObjectB newObjectB(int seed) {
		return new ObjectB(
			((seed % 2)==0),
			(byte)(seed * 10),
			String.valueOf(seed).charAt(0),
			(short)(seed * 20),
			(int)(seed * 30),
			(long)(seed * 40),
			(float)(seed * 1.1),
			(double)(seed * 2.1),
			String.valueOf(seed),       
			new Boolean(((seed % 2)==0)),
			new Byte((byte)(seed * 10)),
			new Character(String.valueOf(seed).charAt(0)),
			new Short((short)(seed * 20)),
			new Integer(seed * 30),
			new Long(seed * 40),
			new Float((float)seed * 1.1),
			new Double((double)seed * 2.1),
			new BigDecimal((double)seed * 1.99999999999999),
			new Date(seed * 99999999),         
			null
		);
	}
}

class ObjectA {
	
	private static ThreadLocal<ObjectComparator> comparator = new ThreadLocal<ObjectComparator>() {
		protected ObjectComparator initialValue() {
			return new ObjectComparator(
				ObjectA.class,
				new Member(boolean.class, "priBoolean"),
				new Member(byte.class, "priByte"),
				new Member(char.class, "priChar"),
				new Member(short.class, "priShort"),
				new Member(int.class, "priInt"),
				new Member(long.class, "priLong"),
				new Member(float.class, "priFloat"),
				new Member(double.class, "priDouble"),
				new Member(String.class, "objString"),
				new Member(Boolean.class, "objBoolean"),
				new Member(Byte.class, "objByte"),
				new Member(Character.class, "objChar"),
				new Member(Short.class, "objShort"),
				new Member(Integer.class, "objInteger"),
				new Member(Long.class, "objLong"),
				new Member(Float.class, "objFloat"),
				new Member(Double.class, "objDouble"),
				new Member(BigDecimal.class, "objBigDecimal"),
				new Member(Date.class, "objDate"),
				new Member(List.class, "lists")
			);
		};
	};
	
	@SuppressWarnings("unused")
    private boolean priBoolean;
	@SuppressWarnings("unused")
	private byte priByte;
	@SuppressWarnings("unused")
	private char priChar;
	@SuppressWarnings("unused")
	private short priShort;
	@SuppressWarnings("unused")
	private int priInt;
	@SuppressWarnings("unused")
	private long priLong;
	@SuppressWarnings("unused")
	private float priFloat;
	@SuppressWarnings("unused")
	private double priDouble;
	@SuppressWarnings("unused")
	private String objString;
	@SuppressWarnings("unused")
	private Boolean objBoolean;
	@SuppressWarnings("unused")
	private Byte objByte;
	@SuppressWarnings("unused")
	private Character objChar;
	@SuppressWarnings("unused")
	private Short objShort;
	@SuppressWarnings("unused")
	private Integer objInteger;
	@SuppressWarnings("unused")
	private Long objLong;
	@SuppressWarnings("unused")
	private Float objFloat;
	@SuppressWarnings("unused")
	private Double objDouble;
	@SuppressWarnings("unused")
	private BigDecimal objBigDecimal;
	@SuppressWarnings("unused")
	private Date objDate;
	private List<ObjectB> lists;

	public ObjectA(boolean priBoolean, byte priByte, char priChar, short priShort, int priInt, long priLong, float priFloat,
                   double priDouble, String objString, Boolean objBoolean, Byte objByte, Character objChar, Short objShort, 
                   Integer objInteger, Long objLong, Float objFloat, Double objDouble, BigDecimal objBigDecimal,
                   Date objDate, List<ObjectB> lists) {
        this.priBoolean = priBoolean;
        this.priByte = priByte;
        this.priChar = priChar;
        this.priShort = priShort;
        this.priInt = priInt;
        this.priLong = priLong;
        this.priFloat = priFloat;
        this.priDouble = priDouble;
        this.objString = objString;
        this.objBoolean = objBoolean;
        this.objByte = objByte;
        this.objChar = objChar;
        this.objShort = objShort;
        this.objInteger = objInteger;
        this.objLong = objLong;
        this.objFloat = objFloat;
        this.objDouble = objDouble;
        this.objBigDecimal = objBigDecimal;
        this.objDate = objDate;
        this.lists = lists;
    }
	public void add(ObjectB obj) {
		if (lists == null) {
			lists = new ArrayList<ObjectB>();
		}
		lists.add(obj);
	}
	
	@Override
	public boolean equals(Object obj) {
		return comparator.get().compare(this, obj);
	}
	
}

class ObjectB {
	private static ThreadLocal<ObjectComparator> comparator = new ThreadLocal<ObjectComparator>() {
		protected ObjectComparator initialValue() {
			return new ObjectComparator(
				ObjectB.class,
				new Member(boolean.class, "priBoolean"),
				new Member(byte.class, "priByte"),
				new Member(char.class, "priChar"),
				new Member(short.class, "priShort"),
				new Member(int.class, "priInt"),
				new Member(long.class, "priLong"),
				new Member(float.class, "priFloat"),
				new Member(double.class, "priDouble"),
				new Member(String.class, "objString"),
				new Member(Boolean.class, "objBoolean"),
				new Member(Byte.class, "objByte"),
				new Member(Character.class, "objChar"),
				new Member(Short.class, "objShort"),
				new Member(Integer.class, "objInteger"),
				new Member(Long.class, "objLong"),
				new Member(Float.class, "objFloat"),
				new Member(Double.class, "objDouble"),
				new Member(BigDecimal.class, "objBigDecimal"),
				new Member(Date.class, "objDate"),
				new Member(List.class, "lists")
			);
		};
	};
	
	@SuppressWarnings("unused")
	private boolean priBoolean;
	@SuppressWarnings("unused")
	private byte priByte;
	@SuppressWarnings("unused")
	private char priChar;
	@SuppressWarnings("unused")
	private short priShort;
	@SuppressWarnings("unused")
	private int priInt;
	@SuppressWarnings("unused")
	private long priLong;
	@SuppressWarnings("unused")
	private float priFloat;
	@SuppressWarnings("unused")
	private double priDouble;
	@SuppressWarnings("unused")
	private String objString;
	@SuppressWarnings("unused")
	private Boolean objBoolean;
	@SuppressWarnings("unused")
	private Byte objByte;
	@SuppressWarnings("unused")
	private Character objChar;
	@SuppressWarnings("unused")
	private Short objShort;
	@SuppressWarnings("unused")
	private Integer objInteger;
	@SuppressWarnings("unused")
	private Long objLong;
	@SuppressWarnings("unused")
	private Float objFloat;
	@SuppressWarnings("unused")
	private Double objDouble;
	@SuppressWarnings("unused")
	private BigDecimal objBigDecimal;
	@SuppressWarnings("unused")
	private Date objDate;
	private List<ObjectB> lists;

	public ObjectB(boolean priBoolean, byte priByte, char priChar, short priShort, int priInt, long priLong, float priFloat,
                   double priDouble, String objString, Boolean objBoolean, Byte objByte, Character objChar, Short objShort,
                   Integer objInteger, Long objLong, Float objFloat, Double objDouble, BigDecimal objBigDecimal,
                   Date objDate, List<ObjectB> lists) {
        this.priBoolean = priBoolean;
        this.priByte = priByte;
        this.priChar = priChar;
        this.priShort = priShort;
        this.priInt = priInt;
        this.priLong = priLong;
        this.priFloat = priFloat;
        this.priDouble = priDouble;
        this.objString = objString;
        this.objBoolean = objBoolean;
        this.objByte = objByte;
        this.objChar = objChar;
        this.objShort = objShort;
        this.objInteger = objInteger;
        this.objLong = objLong;
        this.objFloat = objFloat;
        this.objDouble = objDouble;
        this.objBigDecimal = objBigDecimal;
        this.objDate = objDate;
        this.lists = lists;
    }
	public void add(ObjectB obj) {
		if (lists == null) {
			lists = new ArrayList<ObjectB>();
		}
		lists.add(obj);
	}

	@Override
	public boolean equals(Object obj) {
		return comparator.get().compare(this, obj);
	}
}
