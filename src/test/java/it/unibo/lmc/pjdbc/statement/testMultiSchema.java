package it.unibo.lmc.pjdbc.statement;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;

public class testMultiSchema extends TestCase {

	private Connection conn = null;
	private Statement stmt = null;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testMultiSchema(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {

		Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
		
		// SENZA METADATI
		conn = DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db");
		stmt = conn.createStatement();

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
		File propFile = new File(userDir + "/target/classes/prolog.db.properties");
	    
	    // carico eventuali opzioni
	    if ( propFile.exists() ) {
	    	try {
	    		properties.load(new FileInputStream(propFile));
	    	} catch (Exception e) {
	    		System.out.println("><"+e.getLocalizedMessage());
			}
	    }
	    
	    PropertyConfigurator.configure(properties);
		
		TestSuite ts = new TestSuite();
		
		ts.addTest(new testMultiSchema("testSimpleQuery"));
		ts.addTest(new testMultiSchema("testExplicitTableQuery"));
		ts.addTest(new testMultiSchema("testExplicitSchemaQuery"));
		ts.addTest(new testMultiSchema("testExplicitMultiSchemaQuery"));	
		
		return ts;
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSimpleQuery() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSimpleQuery      ");
 		System.out.println(" ====================== ");
		
		try {
			
			
			ResultSet rs =  stmt.executeQuery("select id from employee;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testExplicitTableQuery() {
		
		System.out.println(" ====================== ");
		System.out.println("  testExplicitTableQuery");
 		System.out.println(" ====================== ");
		
		try {
			
						
			ResultSet rs = stmt.executeQuery("select employee.id from employee;");
					
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testExplicitSchemaQuery() {
		
		System.out.println(" =========================     ");
		System.out.println("  testExplicitSchemaQuery      ");
 		System.out.println(" =========================     ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select prolog_with_meta.employee.id from prolog_with_meta.employee;");

			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testExplicitMultiSchemaQuery() {
		
		System.out.println(" =========================     ");
		System.out.println("  testExplicitMultiSchemaQuery ");
 		System.out.println(" =========================     ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select prolog_with_meta.employee.id, prolog.dept.$0 from prolog_with_meta.employee , prolog.dept;");

			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	
}