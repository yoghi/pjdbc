package it.unibo.lmc.pjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class testConnection extends TestCase {

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testConnection(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {	
		Class.forName("it.unibo.lmc.pjdbc.PrologDriver");
		super.setUp();
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite();
		ts.addTest(new testConnection("testWithoutMetabase"));
		ts.addTest(new testConnection("testWithMetabase"));
		ts.addTest(new testConnection("testMultiple"));
		return ts;
	}
	
	/**
	 * TEST: creo una connesione ad un db senza metabase 
	 */
	public void testWithoutMetabase() {
		
		System.out.println(" ====================== ");
		System.out.println("  testWithoutMetabase   ");
 		System.out.println(" ====================== ");
		
		try {
			
			Connection conn = DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
			
		} catch (Exception e) {
			fail(" il Driver ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: creo una connesione ad un db con metabase 
	 */
	public void testWithMetabase() {
		
		System.out.println(" ====================== ");
		System.out.println("  testWithMetabase      ");
 		System.out.println(" ====================== ");
		
		try {
			
			Connection conn = DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db");
			
		} catch (Exception e) {
			fail(" il Driver ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: creo piu connesioni allo stesso DB
	 */
	public void testMultiple() {
		
		System.out.println(" ====================== ");
		System.out.println("  testMultiple          ");
 		System.out.println(" ====================== ");
		
		try {
			
			Connection conn = DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
			
			Connection conn2 = DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
			
			assertNotSame(conn, conn2);
			
		} catch (Exception e) {
			fail(" il Driver ha ritornato: " + e);
		}
		
		//assertTrue(true);
		
	}
	
}