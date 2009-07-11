package it.unibo.lmc.pjdbc.core;


import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.meta.MCatalog;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;

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
		this.currentDir = userDir + "/target/classes/database/";
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
		
		TestSuite ts = new TestSuite();
		ts.addTest(new testPrologDatabase("testCreateSchema"));
//		ts.addTest(new testPrologDatabase("testCatalog"));
//		ts.addTest(new testPrologDatabase("testSimpleSchema"));
//		ts.addTest(new testPrologDatabase("testInvalidSchema"));
//		ts.addTest(new testPrologDatabase("testMultiSchema"));
		return ts;
	}
	
	
	public void testCatalog() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testCatalog           ");
 		System.out.println(" ====================== ");
		
 		try {
 			
			PrologDatabase db = new PrologDatabase(this.currentDir,null);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
 		
 		assertTrue(true);
	}
	
	/**
	 * TEST: provo a selezionare un semplice schema db
	 * @throws InvalidTheoryException 
	 * @throws PSQLException 
	 */
	public void testSimpleSchema() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testSimpleSchema      ");
 		System.out.println(" ====================== ");
		
 		try {
 			
			new PrologDatabase(this.currentDir,"prolog1");
			
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		} catch (PSQLException e) {
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
 			
			new PrologDatabase(this.currentDir+"prolog.sql",null);
			
		} catch (IOException e) {
			assertTrue(true);
			return;
		} catch (PSQLException e) {
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
 			
			new PrologDatabase(this.currentDir,null);
			
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		} catch (PSQLException e) {
			fail(e.getLocalizedMessage());
		}
 		
 		assertTrue(true);
	}
	
	/**
	 * 
	 * @throws InvalidTheoryException
	 */
	public void testCreateSchema() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testCreateSchema      ");
 		System.out.println(" ====================== ");
		
 		try {
 			
			PrologDatabase p = new PrologDatabase(this.currentDir+"test2.db",null);
			
			MCatalog cat = p.getCatalogInfo();
			
			//p.executeUpdate("..create...");
			
			for (String name : cat.getListSchemaName()) {
				System.out.println(" -- " + name + " -- " );
				MSchema mschema = cat.getMetaSchemaFromName(name);
				for (String tname : mschema.getListTableName()) {
					System.out.println(": "+tname);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (PSQLException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
 		
 		assertTrue(true);
	}
	
}