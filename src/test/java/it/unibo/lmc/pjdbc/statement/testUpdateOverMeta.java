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

public class testUpdateOverMeta extends TestCase {

	private Connection conn = null;
	private Statement stmt = null;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testUpdateOverMeta(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {

		Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
		
		// SENZA METADATI
		conn = DriverManager.getConnection("jdbc:prolog:target/classes/database"); //prolog_with_meta.db
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
		
		ts.addTest(new testUpdateOverMeta("testDropTable"));
		ts.addTest(new testUpdateOverMeta("testDeleteRowTable"));
		ts.addTest(new testUpdateOverMeta("testUpdateRowTable"));
	
		return ts;
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testDropTable() {
		
		System.out.println(" ====================== ");
		System.out.println("  testDropTable         ");
 		System.out.println(" ====================== ");
		
		try {
			
			int n = stmt.executeUpdate("drop table prolog1.employee;");
			
			assertEquals(4, n);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testDeleteRowTable() {
		
		System.out.println(" ====================== ");
		System.out.println("  testDeleteRowTable    ");
 		System.out.println(" ====================== ");
		
		try {
			
			int n = stmt.executeUpdate("delete from prolog1.dept where dept.id = 1;");
			
			assertEquals(1, n);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testDeleteAllRowTable() {
		
		System.out.println(" ====================== ");
		System.out.println("  testDeleteAllRowTable ");
 		System.out.println(" ====================== ");
		
		try {
			
			int n = stmt.executeUpdate("delete from prolog1.dept;");
			
			assertEquals(3, n);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testUpdateRowTable() {
		
		System.out.println(" ====================== ");
		System.out.println("  testUpdateRowTable    ");
 		System.out.println(" ====================== ");
		
		try {
			
			int n = stmt.executeUpdate("update prolog1.age SET age = 5 WHERE name = 'smith' ;");
			
			assertEquals(1, n);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
//	private static void outputResultSet(ResultSet rs) throws Exception {
//	    ResultSetMetaData rsMetaData = rs.getMetaData();
//	    int numberOfColumns = rsMetaData.getColumnCount();
//	    for (int i = 1; i < numberOfColumns + 1; i++) {
//	      String columnName = rsMetaData.getColumnName(i);
//	      System.out.print(columnName + "   ");
//
//	    }
//	    System.out.println();
//	    System.out.println("----------------------");
//
//	    while (rs.next()) {
//	      for (int i = 1; i < numberOfColumns + 1; i++) {
//	        System.out.print(rs.getString(i) + "   ");
//	      }
//	      System.out.println();
//	    }
//
//	  }
	
}