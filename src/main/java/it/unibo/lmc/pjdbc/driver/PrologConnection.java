package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.ParseException;
import it.unibo.lmc.pjdbc.parser.Psql;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

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
	private PrologDatabase db;
	
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
	 *  	<li>database_location : indico il percorso per raggiungere il database (host@port or filename)</li>
	 *  	<li>database_name : se il database è un contenitore (catalog) di piu database(schema) scelgo quale usare</li>
	 * 	</ul>
	 * @throws SQLException ...
	 * TODO gestire le ecezzioni 
	 */
	public PrologConnection(String url_totale, String url_specifico) throws SQLException {

		try {
			
			String[] url = url_specifico.split(":");
			this.sourceUrl = url[0];
			if ( url.length == 2 ) this.databaseName = url[1];

			if ( this.sourceUrl.contains("@") ) {	//remote
				/*
				String[] hs = this.sourceUrl.split("@");
				String host = hs[0];
				int port = Integer.parseInt(hs[1]);
				this.db = new PrologRemoteDB();
				*/
			} else {	//file
				//this.db = PrologDaemon.openDatabase(this.sourceUrl);
				this.db = new PrologDatabase(this.sourceUrl,this.databaseName);
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
		//PrologDaemon.close(this.sourceUrl);
		this.db.close();
	}

	
	public void commit() throws SQLException {
		
	}

	
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
			//TODO da usare PArray
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
		throw new PSQLException("createStatement{int,int} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		throw new PSQLException("createStatement{int,int,int} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		throw new PSQLException("createStruct non yet implemented ", PSQLState.NOT_IMPLEMENTED);
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
		return "";
	}

	
	public int getHoldability() throws SQLException {
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	/**
	 * Restituisce un wrapper per ottenere MetaDati
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return new PrologMetaData(this.db,this);
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

	/**
     * Converts the given SQL statement into the system's native SQL grammar.
     * A driver may convert the JDBC SQL grammar into its system's
     * native SQL grammar prior to sending it. This method returns the
     * native form of the statement that the driver would have sent.
     *
     * @param sql an SQL statement that may contain one or more '?'
     * parameter placeholders
     * @return the native form of this statement
     * @exception SQLException if a database access error occurs
     */	
	public String nativeSQL(String sql) throws SQLException {
		
		StringReader currentQuery = new StringReader(sql);
		Psql parse = new Psql(currentQuery);
		
		ParsedCommand pRequest = null;
		
		try {
			pRequest = parse.parseIt();
		} catch (ParseException e) {
			throw new PSQLException(e.getMessage(),PSQLState.SYNTAX_ERROR);
		}
		return pRequest.toString();
		
	}

	
	public CallableStatement prepareCall(String sql) throws SQLException {
		throw new PSQLException("prepareCall non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public CallableStatement prepareCall(String sql, int resultSetType,int resultSetConcurrency) throws SQLException {
		throw new PSQLException("prepareCall{sql,int,int} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		throw new PSQLException("prepareCall{sql,int,int,int} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		throw new PSQLException("prepareStatement{sql} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw new PSQLException("prepareStatement{sql,int} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		throw new PSQLException("prepareStatement{sql,int[]} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		throw new PSQLException("prepareStatement{sql,string[]} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		throw new PSQLException("prepareStatement{sql,int,int} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		throw new PSQLException("prepareStatement{sql,int,int,int} non yet implemented ", PSQLState.NOT_IMPLEMENTED);
	}

	
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		//TODO da fare
	}

	
	public void rollback() throws SQLException {
		//TODO da fare
	}

	
	public void rollback(Savepoint savepoint) throws SQLException {
		//TODO da fare
	}

	
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.autoCommit = autoCommit;
	}

	
	public void setCatalog(String catalog) throws SQLException {
		//this.databaseName
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
