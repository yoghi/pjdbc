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

public class testSelectOverMeta extends TestCase {

	private Connection conn = null;
	private Statement stmt = null;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testSelectOverMeta(String testName) {
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
		
	    //File propFile = new File("/mnt/store/workspace/Java/Pjdbc/target/classes/prolog.db.properties");
		File propFile = new File("/Users/Yoghi/Workspace/Java/Pjdbc/target/classes/prolog.db.properties");
	    
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
		
		ts.addTest(new testSelectOverMeta("testExecuteQuery"));
		ts.addTest(new testSelectOverMeta("testMultiVarSelect"));
		ts.addTest(new testSelectOverMeta("testVarOverSizeTableSelect"));
		ts.addTest(new testSelectOverMeta("testAliasSelect"));
		ts.addTest(new testSelectOverMeta("testAliasSelectMisc"));
//		ts.addTest(new testSelectOverMeta("testSelectWhere"));
//		ts.addTest(new testSelectOverMeta("testSelectWhere2"));
//		ts.addTest(new testSelectOverMeta("testSelectWhereAND"));
//		ts.addTest(new testSelectOverMeta("testSelectWhereOR"));
//		ts.addTest(new testSelectOverMeta("testSelectWhereOR2"));
		
		
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
			
			ResultSet rs = stmt.executeQuery("select id from employee;");
			
			while (rs.next()) {

				//NB: le colonne si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("id");
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
			
			ResultSet rs = stmt.executeQuery("select id,salary from employee;");
			
			while (rs.next()) {

				//NB: le colonne si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("id");
				assertEquals(x, x2);
				
				float y = rs.getFloat(3);
				float y2 = rs.getFloat("salary");
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
			
			rs = stmt.executeQuery("select id,$5 from employee;");
			
			rs.next();
			//NB: le colonne si contano da 1
			int x = rs.getInt(1);
			int x2 = rs.getInt("id");
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
			
			ResultSet rs = stmt.executeQuery("select e.id,e.name,d.department from employee as e, dept as d;");
			
			while(rs.next()){
				//NB: le colonne si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("id");
				assertEquals(x, x2);
				
				//NB: le colonne si contano da 1
				String s = rs.getString(2);
				String s2 = rs.getString("name");
				assertEquals(s, s2);
				
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
	public void testAliasSelectMisc() {
		
		System.out.println(" ====================== ");
		System.out.println("  testAliasSelectMisc       ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt.executeQuery("select e.id,name,d.department from employee as e, dept as d;");
			
			while(rs.next()){
				//NB: le colonne si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("e.id");
				assertEquals(x, x2);
				
				//NB: le colonne si contano da 1
				String s = rs.getString(4);
				String s2 = rs.getString("department");
				assertEquals(s, s2);
				
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