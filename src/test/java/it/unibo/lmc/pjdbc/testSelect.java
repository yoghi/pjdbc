package it.unibo.lmc.pjdbc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testSelect extends TestCase {

	private Connection conn = null;
	private Connection conn_meta = null;
	private Statement stmt = null;
	private Statement stmt_meta = null;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testSelect(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {

		Class.forName("it.unibo.lmc.pjdbc.PrologDriver");

		//String systemOs = System.getProperty("os.name");
		
		/*
		if ( systemOs == "Windows XP" ) {
		    conn = DriverManager.getConnection("jdbc:prolog:"+rootWin+fileName);
		}
		
		if ( systemOs == "Darwin" ) {
		        conn = DriverManager.getConnection("jdbc:prolog:"+rootMac+fileName);
		}
		*/
		
		// SENZA METADATI
		conn = DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
		stmt = conn.createStatement();
		
		// CON METADATI
		conn_meta = DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db");
		stmt_meta = conn.createStatement();

		super.setUp();
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite();
		ts.addTest(new testSelect("testExecuteQuery"));
		ts.addTest(new testSelect("testSimpleSelect"));
		ts.addTest(new testSelect("testSimpleSelectWithoutMeta"));
		ts.addTest(new testSelect("testGenericCampSelect"));
		ts.addTest(new testSelect("testWhereSelect"));
		return ts;
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testExecuteQuery() {
		
		System.out.println(" ====================== ");
		System.out.println("  testExecuteQuery      ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_meta.executeQuery("select name from employee;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}

	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSimpleSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSimpleSelect      ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_meta.executeQuery("select name from employee;");
			while(rs.next()) {
                String name = rs.getString("name");
                System.out.println("NAME: "+name.toString());
			}
			assertTrue(true);
			
		} catch (Exception e) {
			fail("testSimpleSelect " + e);
		}
		
	}
	
	/**
	 * TEST: Select di un campo specifico su un db senza metadati
	 */
	public void testSimpleSelectWithoutMeta() {
		
		System.out.println(" ============================= ");
		System.out.println("  testSimpleSelectWithoutMeta  ");
 		System.out.println(" ============================= ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select name from employee;");
			while(rs.next()) {
                String name = rs.getString("name");
                System.out.println("NAME: "+name.toString());
			}
			assertTrue(true);
			
		} catch (Exception e) {
			fail("testSimpleSelect " + e);
		}
		
	}
	
	/**
	 * TEST: Select di un campo generico
	 */
	public void testGenericCampSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testGenericCampSelect      ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select $0 from employee;");
			while(rs.next()) {
				String campo = rs.getString(0);
                System.out.println("ID: "+campo.toString());
			}
			assertTrue(true);
			
		} catch (Exception e) {
			fail("testSimpleSelect " + e);
		}
		
	}
	
	/**
	 * TEST: Select con clusola where esplicitata 
	 */
	public void testWhereSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testWhereSelect      ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select $0 from employee where $0=1 ;");
			String id = rs.getString(0);
			assertEquals(id, "1");
			
		} catch (Exception e) {
			fail("testSimpleSelect " + e);
		}
		
	}

}
