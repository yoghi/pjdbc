package it.unibo.lmc.pjdbc.statement;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class testSelectOverNoMeta extends TestCase {

	private Connection conn = null;
	private Statement stmt = null;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testSelectOverNoMeta(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {

		Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
		
		// SENZA METADATI
		conn = DriverManager.getConnection("jdbc:prolog:target/classes/database/prolog.db");
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
		
		ts.addTest(new testSelectOverNoMeta("testExecuteQuery"));
		ts.addTest(new testSelectOverNoMeta("testMultiVarSelect"));
		ts.addTest(new testSelectOverNoMeta("testVarOverSizeTableSelect"));
		ts.addTest(new testSelectOverNoMeta("testAliasSelect"));
		ts.addTest(new testSelectOverNoMeta("testAliasSelectMisc"));
		ts.addTest(new testSelectOverNoMeta("testSelectWhere"));
//		ts.addTest(new testSelectOverNoMeta("testSelectWhere2"));
//		ts.addTest(new testSelectOverNoMeta("testSelectWhereAND"));
//		ts.addTest(new testSelectOverNoMeta("testSelectWhereOR"));
//		ts.addTest(new testSelectOverNoMeta("testSelectWhereOR2"));
		
		
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
			
			ResultSet rs = stmt.executeQuery("select $0 from employee;");
			
			while (rs.next()) {

				//NB: le colonne si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("$0");
				assertEquals(x, x2);

			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testMultiVarSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testMultiVarSelect    ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select $0,$2 from employee;");
			
			while (rs.next()) {

				//NB: le colonne si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("$0");
				assertEquals(x, x2);
				
				float y = rs.getFloat(2);
				float y2 = rs.getFloat("$2");
				assertEquals(y, y2);

			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testVarOverSizeTableSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testVarOverSizeTableSelect      ");
 		System.out.println(" ====================== ");
		
 		ResultSet rs = null;
		try {
			
			rs = stmt.executeQuery("select $0,$5 from employee;");
			
			rs.next();
			//NB: le colonne si contano da 1
			int x = rs.getInt(1);
			int x2 = rs.getInt("$0");
			assertEquals(x, x2);
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		try {
			
			int y = rs.getInt(6);
			int y2 = rs.getInt("$5");
			assertEquals(y, y2);
			
		} catch (SQLException e) {
			assertTrue(true);
			return;
		}
		
		fail("errore non propagato");
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testAliasSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testAliasSelect       ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testAliasSelectMisc() {
		
		System.out.println(" ====================== ");
		System.out.println("  testAliasSelectMisc       ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.$0,$1,d.$1 from employee as e, dept as d;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhere() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhere       ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where (e.$0 = d.$1);");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhere2() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhere2      ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where (e.$0 = ( d.$1 + 1) );");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhereAND() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhereAND    ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where ( (e.$0 = d.$1) AND (e.$0 > 1) ) ;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhereOR() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhereOR    ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where ( (e.$0 > 1000) OR (d.$1 > 1) ) ;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhereOR2() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhereOR2    ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.$1,d.$0 from employee as e , dept as d , eta as et where ( ( (e.$0 = d.$1) AND ( e.$1 = et.$0 ) ) AND ( (et.$1 < 40) OR ( e.$2 > 2000 ) ) ) ;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
}