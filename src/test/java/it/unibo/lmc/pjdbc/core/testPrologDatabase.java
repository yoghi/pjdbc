package it.unibo.lmc.pjdbc.core;


import it.unibo.lmc.pjdbc.database.PrologDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;

import alice.tuprolog.InvalidTheoryException;

public class testPrologDatabase extends TestCase {

	private String currentDir;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testPrologDatabase(String testName) {
		super(testName);
		
		String userDir = System.getProperty("user.dir");
		this.currentDir = userDir + "/target/classes/";
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
		ts.addTest(new testPrologDatabase("testSimpleSchema"));
		ts.addTest(new testPrologDatabase("testFilterSchema"));
		ts.addTest(new testPrologDatabase("testInvalidSchema"));
		ts.addTest(new testPrologDatabase("testMultiSchema"));
		return ts;
	}
	
	/**
	 * TEST: provo a selezionare un semplice schema db
	 * @throws InvalidTheoryException 
	 */
	public void testSimpleSchema() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testSimpleSchema      ");
 		System.out.println(" ====================== ");
		
 		try {
 			
			PrologDatabase.getInstance(this.currentDir+"prolog.db");
			
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
 		
 		assertTrue(true);
	}
	
	/**
	 * TEST: filtro gli schemi in base all'estensione
	 * @throws InvalidTheoryException 
	 */
	public void testFilterSchema() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testFilterSchema      ");
 		System.out.println(" ====================== ");
		
 		try {
 			
			PrologDatabase.getInstance(this.currentDir,"db");
			
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
		
		assertTrue(true);
	}
 	
	
	/**
	 * TEST: provo a selezionare un file non valido
	 * @throws InvalidTheoryException 
	 */
	public void testInvalidSchema() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testInvalidSchema      ");
 		System.out.println(" ====================== ");
		
 		try {
 			
			PrologDatabase.getInstance(this.currentDir+"prolog.sql");
			
		} catch (IOException e) {
			assertTrue(true);
			return;
		}
		
		fail("il file non era un db valido e il sistema non se ne è accorto!");
 		
	}
	
	/**
	 * 
	 * @throws InvalidTheoryException
	 */
	public void testMultiSchema() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testMultiSchema       ");
 		System.out.println(" ====================== ");
		
 		try {
 			
			PrologDatabase.getInstance(this.currentDir);
			
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
 		
 		assertTrue(true);
	}
	
}