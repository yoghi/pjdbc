package it.unibo.lmc.pjdbc.driver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class PrologConnectionTest extends TestCase {

	static private Connection connection;
	
	public PrologConnectionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public final void testPrologConnection() {
		try {
			String user_dir = System.getProperty("user.dir");
			connection  = DriverManager.getConnection("jdbc:prolog:"+user_dir+"/target/classes/database/catalog1:prolog1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public final void testClearWarnings() {
		fail("Not yet implemented"); 
	}

	public final void testClose() {
		fail("Not yet implemented"); 
	}

	public final void testCommit() {
		fail("Not yet implemented"); 
	}

	public final void testCreateArrayOf() {
		fail("Not yet implemented"); 
	}

	public final void testCreateBlob() {
		fail("Not yet implemented"); 
	}

	public final void testCreateClob() {
		fail("Not yet implemented"); 
	}

	public final void testCreateStatement() {
		try {
			Statement stmt = connection.createStatement();
			assertNotNull(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	public final void testCreateStatementIntInt() {
		fail("Not yet implemented"); 
	}

	public final void testCreateStatementIntIntInt() {
		fail("Not yet implemented"); 
	}

	public final void testCreateStruct() {
		fail("Not yet implemented"); 
	}

	public final void testGetAutoCommit() {
		try {
			boolean commit = connection.getAutoCommit();
			assertEquals(commit, true);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage()); 
		}
	}

	public final void testGetCatalog() {
		try {
			String name = connection.getCatalog();	//TODO da sistemare
			assertEquals("", name);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	public final void testGetClientInfo() {
		fail("Not yet implemented"); 
	}

	public final void testGetClientInfoString() {
		fail("Not yet implemented"); 
	}

	public final void testGetHoldability() {
		fail("Not yet implemented"); 
	}

	public final void testGetMetaData() {
		try {
			DatabaseMetaData meta = connection.getMetaData();
			assertNotNull(meta);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	public final void testGetTransactionIsolation() {
		fail("Not yet implemented"); 
	}

	public final void testGetTypeMap() {
		fail("Not yet implemented"); 
	}

	public final void testGetWarnings() {
		fail("Not yet implemented"); 
	}

	public final void testIsClosed() {
		fail("Not yet implemented"); 
	}

	public final void testIsReadOnly() {
		fail("Not yet implemented"); 
	}

	public final void testIsValid() {
		fail("Not yet implemented"); 
	}

	public final void testNativeSQL() {
		
		try {
			
			String nSql = connection.nativeSQL("select * from employee;");
			assertEquals("select [*] from [employee]", nSql);
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		
	}

	public final void testReleaseSavepoint() {
		fail("Not yet implemented"); 
	}

	public final void testRollback() {
		fail("Not yet implemented"); 
	}

	public final void testRollbackSavepoint() {
		fail("Not yet implemented"); 
	}

	public final void testSetAutoCommit() {
		try {
			connection.setAutoCommit(false);
			boolean commit = connection.getAutoCommit();
			assertEquals(false, commit);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	public final void testSetCatalog() {
		
		/**
	     * Sets the given catalog name in order to select 	
	     * a subspace of this <code>Connection</code> object's database 
	     * in which to work.
	     * <P>
	     * If the driver does not support catalogs, it will
	     * silently ignore this request.
	     *
	     * @param catalog the name of a catalog (subspace in this 
	     *        <code>Connection</code> object's database) in which to work
	     * @exception SQLException if a database access error occurs
	     * @see #getCatalog
	     */
		
		fail("Not yet implemented"); 
	}

	public final void testSetHoldability() {
		fail("Not yet implemented"); 
	}

	public final void testSetReadOnly() {
		fail("Not yet implemented"); 
	}

	public final void testSetSavepoint() {
		fail("Not yet implemented"); 
	}

	public final void testSetSavepointString() {
		fail("Not yet implemented"); 
	}

	public final void testSetTransactionIsolation() {
		fail("Not yet implemented"); 
	}

	public final void testSetTypeMap() {
		fail("Not yet implemented"); 
	}

//	public final void testIsWrapperFor() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testUnwrap() {
//		fail("Not yet implemented"); 
//	}
//	
//	public final void testPrepareCallString() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareCallStringIntInt() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareCallStringIntIntInt() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareStatementString() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareStatementStringInt() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareStatementStringIntArray() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareStatementStringStringArray() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareStatementStringIntInt() {
//		fail("Not yet implemented"); 
//	}
//
//	public final void testPrepareStatementStringIntIntInt() {
//		fail("Not yet implemented"); 
//	}

}
