package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.database.utils.PTypes;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class PrologResultSetMetaDataTest extends TestCase {

	static private Connection c;
	private Statement stmt;
	static private ResultSet res;
	
	public PrologResultSetMetaDataTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		if ( null == c ) c  = DriverManager.getConnection("jdbc:prolog:target/classes/database/catalog1/:prolog1"); 
	    stmt = this.c.createStatement();
		res = stmt.executeQuery("select * from prolog1.employee;");
	}

	public final void testPrologResultSetMetaData() {
		try {
			ResultSetMetaData meta = res.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
	}

	public final void testGetCatalogName() {
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			String userDir = System.getProperty("user.dir");
			assertEquals(userDir+"/target/classes/database/catalog1", meta.getCatalogName(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
	}

	public final void testGetColumnCount() {
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			assertEquals(3, meta.getColumnCount());
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
	}
	
	public final void testGetColumnLabel() {
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			assertEquals("id", meta.getColumnName(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
	}

	public final void testGetColumnName() {
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			assertEquals("id", meta.getColumnName(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
	}

	public final void testGetColumnType() {
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			assertEquals(PTypes.INT.getSqlType(), meta.getColumnType(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
	}

	public final void testGetColumnTypeName() {
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			assertEquals(PTypes.INT.name(), meta.getColumnTypeName(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
	}
	
	public final void testGetSchemaName() {
		
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			assertEquals("prolog1", meta.getSchemaName(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
		
	}
	
	public final void testGetTableName() {
		
		try {
			
			ResultSetMetaData meta = res.getMetaData();
			assertEquals("employee", meta.getTableName(0));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("error : "+e.getLocalizedMessage());
		}
		
	}

//	public final void testGetPrecision() {
//		fail("Not yet implemented");
//	}
//
//	public final void testGetScale() {
//		fail("Not yet implemented");
//	}
//	
//	public final void testIsAutoIncrement() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsCaseSensitive() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsCurrency() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsDefinitelyWritable() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsNullable() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsReadOnly() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsSearchable() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsSigned() {
//		fail("Not yet implemented");
//	}
//
//	public final void testIsWritable() {
//		fail("Not yet implemented");
//	}
//	
//	public final void testGetColumnClassName() {
//		fail("Not yet implemented");
//	}
//
//	public final void testGetColumnDisplaySize() {
//		fail("Not yet implemented");
//	}

}
