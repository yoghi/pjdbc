package it.unibo.lmc.pjdbc.statement;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;

public class testInsertOverMeta extends TestCase {

	static private Connection conn = null;
	static private Statement stmt = null;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testInsertOverMeta(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {

		Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
		
		// SENZA METADATI
		if ( null == conn ) {
			conn = DriverManager.getConnection("jdbc:prolog:target/classes/database/catalog1/:prolog1");
			stmt = conn.createStatement();
		}

		super.setUp();
	}
	

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		
		/**
		 * Properties / custumization
		 */
		Properties properties = new Properties();
		
		String userDir = System.getProperty("user.dir");
		File propFile = new File(userDir + "/target/classes/common.properties");
	    
	    // carico eventuali opzioni
	    if ( propFile.exists() ) {
	    	try {
	    		properties.load(new FileInputStream(propFile));
	    	} catch (Exception e) {
	    		System.out.println("><"+e.getLocalizedMessage());
			}
	    	
	    }
	    
	    PropertyConfigurator.configure(properties);
		
	    //internal.db
	    
	    
		TestSuite ts = new TestSuite();
		
		ts.addTest(new testInsertOverMeta("testCreate"));
//		ts.addTest(new testInsertOverMeta("testBaseInsert"));
//		ts.addTest(new testInsertOverMeta("testShortInsert"));
//		ts.addTest(new testInsertOverMeta("testInvalidInsert"));
	
		return ts;
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testBaseInsert() {
		
		System.out.println(" ====================== ");
		System.out.println("  testBaseInsert      ");
 		System.out.println(" ====================== ");
		
		try {
			
			int rs = stmt.executeUpdate(" insert into employee (id,salary,name) values (100,1000,'babo');");
			
			assertEquals(1, rs);
			
			stmt.executeQuery(" select * from employee;");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testShortInsert() {
		
		System.out.println(" ====================== ");
		System.out.println("  testShortInsert      ");
 		System.out.println(" ====================== ");
		
		try {
			
			int rs = stmt.executeUpdate(" insert into employee values (100,'babo',1000);");
			
			assertEquals(1, rs);
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testInvalidInsert() {
		
		System.out.println(" ====================== ");
		System.out.println("  testInvalidInsert      ");
 		System.out.println(" ====================== ");
		
		try {
			
			stmt.executeUpdate(" insert into emplyee values (100,'babo',1000);");
			
		} catch (Exception e) {
			assertTrue(true);
			return;
		}
		
		fail(" invalid insert was accepted anyway");
		
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testCreate() {
		
		System.out.println(" ====================== ");
		System.out.println("  testCreate            ");
 		System.out.println(" ====================== ");
		
		try {
			
			int rs = stmt.executeUpdate("create table prova ( id string , age int );");
			assertEquals(1, rs);
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	

	
}