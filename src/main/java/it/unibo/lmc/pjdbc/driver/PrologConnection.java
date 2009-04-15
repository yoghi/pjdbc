package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.core.IDatabase;
import it.unibo.lmc.pjdbc.core.PrologDatabase;
import it.unibo.lmc.pjdbc.core.database.PSchema;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import alice.tuprolog.InvalidTheoryException;

public class PrologConnection implements Connection {

	/**
	 * Prolog Engine
	 */
//	private Prolog dbengine = null;
	
	/**
	 * Nome database
	 */
	private String databaseName;
	
	/**
	 * Filename o host
	 * nome.db o 192.168.2.1:1231@nome
	 */
	private String sourceUrl;
	
	/**
	 * Database
	 */
	private PSchema db;
	
	/**
	 * The transaction isolation level for this
     * <code>Connection</code> object to the one given.
     * Level one of the following <code>Connection</code> constants:
     *        <code>Connection.TRANSACTION_READ_UNCOMMITTED = 1</code>,
     *        <code>Connection.TRANSACTION_READ_COMMITTED = 2</code>,
     *        <code>Connection.TRANSACTION_REPEATABLE_READ = 4</code>, or
     *        <code>Connection.TRANSACTION_SERIALIZABLE = 8</code>.
     *        (Note that <code>Connection.TRANSACTION_NONE</code> cannot be used because it specifies that transactions are not supported.)
     * @see DatabaseMetaData#supportsTransactionIsolationLevel 
     * @see #getTransactionIsolation 
	 */
	private int transactionLevel = 1;
	
	private boolean autoCommit = true;

	/**
	 * Connesione ad un database Prolog 
	 * @param url_totale url passato al factory
	 * @param url_specifico informazioni per accedere al database
	 *  <i>database_location;database_name</i> 
	 * 	<ul>
	 * 		<li>database_name : se il database è un contenitore di piu database scelgo quale usare</li>
	 *  	<li>database_location : indico il percorso per raggiungere il database (host@port or filename)</li>
	 * 	</ul>
	 * @throws SQLException ...
	 * TODO gestire le ecezzioni 
	 */
	public PrologConnection(String url_totale, String url_specifico) throws SQLException {

		try {
			
			String[] url = url_specifico.split(";");
			
			this.sourceUrl = url[0];
			
			if ( url.length == 2 ) {
				this.databaseName = url[1];
			}
			
			if ( this.sourceUrl.contains("@") ) {
//				String[] hs = this.sourceUrl.split("@");
//				String host = hs[0];
//				int port = Integer.parseInt(hs[1]);
				//this.db = new PrologRemoteDB();
			} else {
				//file
				this.db = PrologDatabase.openDatabase(this.sourceUrl); 
				//new PrologLocalDB(this.sourceUrl);
			}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public void clearWarnings() throws SQLException {
		

	}

	
	public void close() throws SQLException {
		PrologDatabase.close(this.sourceUrl);
	}

	
	public void commit() throws SQLException {
		
	}

	
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		
		return null;
	}

	
	public Blob createBlob() throws SQLException {
		
		return null;
	}

	
	public Clob createClob() throws SQLException {
		
		return null;
	}

	
	public Statement createStatement() throws SQLException {
		return new PrologStatement(this, this.db);
	}

	
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return null;
	}

	
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return null;
	}

	
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		
		return null;
	}

	
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}

	/**
	 * Il catalog coincide per semplicità con il database
	 */
	public String getCatalog() throws SQLException {
		return this.databaseName;
	}

	
	public Properties getClientInfo() throws SQLException {
		
		return null;
	}

	
	public String getClientInfo(String name) throws SQLException {
		
		return null;
	}

	
	public int getHoldability() throws SQLException {
		
		return 0;
	}

	/**
	 * Restituisce un wrapper per ottenere MetaDati
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		//return this.db.getMetaData();
		return null;
	}

	
	public int getTransactionIsolation() throws SQLException {
		return this.transactionLevel;
	}

	
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		
		return null;
	}

	
	public SQLWarning getWarnings() throws SQLException {
		
		return null;
	}

	
	public boolean isClosed() throws SQLException {
		
		return false;
	}

	
	public boolean isReadOnly() throws SQLException {
		
		return false;
	}

	
	public boolean isValid(int timeout) throws SQLException {
		
		return false;
	}

	
	public String nativeSQL(String sql) throws SQLException {
		
		return null;
	}

	
	public CallableStatement prepareCall(String sql) throws SQLException {
		
		return null;
	}

	
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		
		return null;
	}

	
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		
		return null;
	}

	
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		

	}

	
	public void rollback() throws SQLException {
		

	}

	
	public void rollback(Savepoint savepoint) throws SQLException {
		

	}

	
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		

	}

	
	public void setCatalog(String catalog) throws SQLException {
		

	}

	
	public void setHoldability(int holdability) throws SQLException {
		

	}

	
	public void setReadOnly(boolean readOnly) throws SQLException {
		

	}

	
	public Savepoint setSavepoint() throws SQLException {
		
		return null;
	}

	
	public Savepoint setSavepoint(String name) throws SQLException {
		
		return null;
	}

	
	public void setTransactionIsolation(int level) throws SQLException {
		this.transactionLevel = level;
		//TODO aggiornare il monitor usato per gestire le transazioni
	}

	
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		

	}

	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		return false;
	}

	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return null;
	}

}
