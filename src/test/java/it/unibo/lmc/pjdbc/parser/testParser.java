package it.unibo.lmc.pjdbc.parser;

import it.unibo.lmc.pjdbc.statement.testSelectOverNoMeta;

import java.io.File;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;

public class testParser extends TestCase {

	/** 
	 * Costruttore 
	 * @param testName
	 */
	public testParser(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {

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
		
	    File propFile = new File("/mnt/store/workspace/Java/Pjdbc/target/classes/prolog.db.properties");
		//File propFile = new File("/Users/Yoghi/Workspace/Java/Pjdbc/target/classes/prolog.db.properties");
	    
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
		
		ts.addTest(new testSelectOverNoMeta("testSelectWhereOR2"));
		
		
		return ts;
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhereOR2() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhereOR2    ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "select e.$1,d.$0 from employee as e , dept as d , eta as et where ( ( (e.$0 = d.$1) AND ( e.$1 = et.$0 ) ) AND ( (et.$1 < 40) OR ( e.$2 > 2000 ) ) ) ;";
			
			
			
			
		} catch (Exception e) {
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}
	
	
}
