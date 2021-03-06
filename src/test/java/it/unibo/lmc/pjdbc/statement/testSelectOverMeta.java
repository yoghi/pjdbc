package it.unibo.lmc.pjdbc.statement;

import it.unibo.lmc.pjdbc.database.utils.PSQLException;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;

public class testSelectOverMeta extends TestCase {

	static private Connection conn = null;
	static private Statement stmt_1 = null;
	static private Statement stmt_2 = null;

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
		if ( null == conn ) conn = DriverManager.getConnection("jdbc:prolog:target/classes/database/catalog1/:prolog1");
		if ( null == stmt_1 ) stmt_1 = conn.createStatement();
		if ( null == stmt_2 ) stmt_2 = DriverManager.getConnection("jdbc:prolog:target/classes/database/catalog1/prolog2.db").createStatement();

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
		
		ts.addTest(new testSelectOverMeta("testExecuteQuery"));
		ts.addTest(new testSelectOverMeta("testMultiVarSelect"));
		ts.addTest(new testSelectOverMeta("testAnyVarSelect"));
		ts.addTest(new testSelectOverMeta("testMultiAnyVarSelect"));
		ts.addTest(new testSelectOverMeta("testVarOverSizeTableSelect"));
		ts.addTest(new testSelectOverMeta("testAliasSelect"));
		ts.addTest(new testSelectOverMeta("testAliasSelectMisc"));
		ts.addTest(new testSelectOverMeta("testGetArray"));
		ts.addTest(new testSelectOverMeta("testGetInvalidArray"));
		ts.addTest(new testSelectOverMeta("testInvalidAliasQuery"));
		ts.addTest(new testSelectOverMeta("testWrongAnyClausola"));
		
		ts.addTest(new testSelectOverMeta("testSelectWhere"));
		ts.addTest(new testSelectOverMeta("testSelectParWhere"));
		ts.addTest(new testSelectOverMeta("testComplexWhere"));
		
//		ts.addTest(new testSelectOverMeta("testSelectWhere2"));
		ts.addTest(new testSelectOverMeta("testSelectWhereAND"));
		ts.addTest(new testSelectOverMeta("testSelectWhereOR"));
		ts.addTest(new testSelectOverMeta("testSelectWhereOR2"));
		
		
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
			
			ResultSet rs = stmt_1.executeQuery("select id from employee;");
			
			while (rs.next()) {

				//NB: le colonne del resultset si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("id");
				assertEquals(x, x2);
				assertNotNull(x);
				
			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			e.printStackTrace();
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
			
			ResultSet rs = stmt_1.executeQuery("select id,salary from employee;");
			
			while (rs.next()) {

				//NB: le colonne del resultset si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("id");
				assertEquals(x, x2);
				assertNotNull(x);
				
				float y = rs.getFloat(2);
				float y2 = rs.getFloat("salary");
				assertEquals(y, y2);
				assertNotNull(y);

			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	public void testAnyVarSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testAnyVarSelect    ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_1.executeQuery("select * from employee;");
			
			while (rs.next()) {

				//NB: le colonne del resultset si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("employee.id");
				assertEquals(x, x2);
				assertNotNull(x);
				
				float y = rs.getFloat(3);
				float y2 = rs.getFloat("employee.salary");
				assertEquals(y, y2);
				assertNotNull(y);

			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}

	public void testMultiAnyVarSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testMultiAnyVarSelect ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_1.executeQuery("select e.*,d.$1 from employee as e,dept as d;");
			
			while (rs.next()) {

				//NB: le colonne del resultset si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("employee.id");
				assertEquals(x, x2);
				assertNotNull(x);
				
				float y = rs.getFloat(3);
				float y2 = rs.getFloat("employee.salary");
				assertEquals(y, y2);
				assertNotNull(y);

			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	public void testGetArray(){
		
		System.out.println(" ====================== ");
		System.out.println("  testGetArray          ");
 		System.out.println(" ====================== ");
		
 		ResultSet rs = null;
 		int i = 1;
		try {
			
			rs = stmt_2.executeQuery("select $1 from multiarray;");
			
			while ( rs.next() ) {
				//NB: le colonne del resultset si contano da 1
				Array x = rs.getArray(1);
				Array x2 = rs.getArray("$1");
				i++;
				
//				assertEquals(x, x2);
//				assertNotNull(x);
			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" ExecuteQuery<"+i+"> ha ritornato: " + e);
		}
		
	}
	
	public void testGetInvalidArray(){
		
		System.out.println(" ====================== ");
		System.out.println("  testGetInvalidArray   ");
 		System.out.println(" ====================== ");
		
 		ResultSet rs = null;
		try {
			
			rs = stmt_2.executeQuery("select $1 from noarray;");
			
			while ( rs.next() ) {

				Array x = rs.getArray(1);
				Array x2 = rs.getArray("$1");
			}
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (PSQLException e) {
			assert(true);
			return;
		} catch (SQLException e) {
			fail(e.getLocalizedMessage());
		}
		
		fail(" getArray non si è accorto che non è un array ");
		
	}
	

	/**
	 * TEST: Count Row
	 */
	public void testCountRowQuery() {
		
		System.out.println(" ====================== ");
		System.out.println("  testCountRowQuery ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_1.executeQuery("select e.id from employee;");
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int NumOfCol=rsmd.getColumnCount();
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			assertTrue(true);
			return;
		}
		fail(" Il sistema non si è accorto che la query è errata (e.id , e not exist! ) ");
		
	}
	
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testInvalidAliasQuery() {
		
		System.out.println(" ====================== ");
		System.out.println("  testInvalidAliasQuery ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_1.executeQuery("select e.id from employee;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			assertTrue(true);
			return;
		}
		fail(" Il sistema non si è accorto che la query è errata (e.id , e not exist! ) ");
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testInvalidField() {
		
		System.out.println(" ====================== ");
		System.out.println("  testInvalidField ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_1.executeQuery("select employee.id from dept;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			assertTrue(true);
			return;
		}
		fail(" Il sistema non si è accorto che la query è errata (employee.id , employee non è la tabella presa nel from! ) ");
		
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
			
			rs = stmt_1.executeQuery("select id,$5 from employee;");
			
			rs.next();
			//NB: le colonne del resultset si contano da 1
			int x = rs.getInt(1);
			int x2 = rs.getInt("id");
			assertEquals(x, x2);
			assertNotNull(x);
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		try {
			
			int y = rs.getInt(6);
			int y2 = rs.getInt("$5");
			
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
			
			ResultSet rs = stmt_1.executeQuery("select e.id as id,e.name as name,d.department from employee as e, dept as d;");
			
			while(rs.next()){
				//NB: le colonne del resultset si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("id");
				assertEquals(x, x2);
				assertNotNull(x);
				
				//NB: le colonne del resultset si contano da 1
				String s = rs.getString(2);
				String s2 = rs.getString("name");
				assertEquals(s, s2);
				assertNotNull(s);
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
			
			ResultSet rs = stmt_1.executeQuery("select e.id,name,d.department from employee as e, dept as d;");
			
			while(rs.next()){
				//NB: le colonne del resultset si contano da 1
				int x = rs.getInt(1);
				int x2 = rs.getInt("e.id");
				assertEquals(x, x2);
				assertNotNull(x);
				
				String name = rs.getString("name"); 
				
				//NB: le colonne del resultset si contano da 1
				String s = rs.getString(3);
				String s2 = rs.getString("d.department");
				assertEquals(s, s2);
				assertNotNull(s);
				
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
			
			ResultSet rs = stmt_1.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where e.$0 = d.$1;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectParWhere() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectParWhere    ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_1.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where (e.$0 != d.$1);");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testComplexWhere() {
		
		System.out.println(" ====================== ");
		System.out.println("  testComplexWhere      ");
 		System.out.println(" ====================== ");
		
		try {
			
			ResultSet rs = stmt_1.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where (e.$0 = d.$1) OR (d.$0 = 'smith') ;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
		} catch (Exception e) {
			e.printStackTrace();
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
			
			ResultSet rs = stmt_1.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where (e.$0 = ( d.$1 + 1) );");
			
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
			
			ResultSet rs = stmt_1.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where ( (e.$0 = d.$1) AND (e.$0 > 1) ) ;");
			
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
			
			ResultSet rs = stmt_1.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where ( (e.$0 > 1000) OR (d.$1 > 1) ) ;");
			
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
			
			ResultSet rs = stmt_1.executeQuery("select e.$1,d.$0 from employee as e , dept as d , age as et where ( ( (e.$0 = d.$1) AND ( e.$1 = et.$0 ) ) AND ( (et.$1 < 40) OR ( e.$2 > 2000 ) ) ) ;");
			
			if (rs == null) fail("ExecuteQuery not return valid ResultSet ");
			
			while(rs.next()){
				System.out.println("nome "+rs.getString(1)+" settore "+rs.getString(2));
			}
			
		} catch (Exception e) {
			fail(" ExecuteQuery ha ritornato: " + e);
		}
		
		assertTrue(true);
		
	}
	
	public void testWrongAnyClausola() {
		
		System.out.println(" ====================== ");
		System.out.println("  testWrongAnyClausola  ");
 		System.out.println(" ====================== ");
		
		try {
			
			stmt_1.executeQuery("select *.$0,d.$1 from employee as e, dept as d;");
			
		} catch (Exception e) {
			assertTrue(true);
			return;
		}
		fail(" Not find error into sql");
		
		
	}
	
}