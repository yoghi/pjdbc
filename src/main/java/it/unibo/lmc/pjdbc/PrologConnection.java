package it.unibo.lmc.pjdbc;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;

import java.util.Map;
import java.util.Properties;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class PrologConnection implements Connection {

	/**
	 * Prolog Engine
	 */
	private Prolog dbengine = null;
	
	/**
	 * Metadati Database
	 */
	private PrologMetaData databaseMetaData = new PrologMetaData();
	
	/**
	 * Url
	 */
	private final String url;
	
	/**
	 * Properties / custumization
	 */
	private Properties properties = null;

	/**
	 * Logger 
	 */
	private Logger log = null;

	public PrologConnection(String url, String filename) throws SQLException {

		try {
			
			properties = new Properties();
			
		    boolean exists = (new File(filename)).exists();
		    
		    if ( exists ) {
		    	properties.load(new FileInputStream(filename+".properties"));
		    }
		    
			this.logger_init();

			this.dbengine = new Prolog();

			Theory t = new Theory(new FileInputStream(filename));
			this.dbengine.setTheory(t);

		} catch (InvalidTheoryException e) {
			throw new SQLException("Prolog database internal error : "
					+ e.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.url = url;
	}

	protected void logger_init() {

		log = Logger.getLogger("PrologConnection");

		log.setLevel(Level.toLevel(properties.getProperty("log_level", "ERROR")));

		PatternLayout p = new PatternLayout();
		p.setConversionPattern("%-4r %p [%t] (%F:%L){%M} %m%n");

		String log_output = properties.getProperty("log_output", "console");

		if (log_output.equalsIgnoreCase("console")) {
			log.addAppender(new ConsoleAppender(p));
		} else if (log_output.equalsIgnoreCase("file")) {
			try {
				
				String filename = properties.getProperty("filename","prologDB");
				
				log.addAppender(new DailyRollingFileAppender(p, properties
						.getProperty("log_file_output", filename
								+ ".log"), properties.getProperty(
						"log_file_rotatedate", "'.'yyyy-MM")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.addAppender(new ConsoleAppender(p));
		}

	}

	/**
	 * Get the prolog engine use from connection
	 * @return Prolog engine
	 * @deprecated
	 */
	public Prolog getEngine() {
		return this.dbengine;
	}

	
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void close() throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void commit() throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Statement createStatement() throws SQLException {

		return new PrologStatement(this, this.dbengine);

	}

	
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non javadoc)
	 * Restituisce un wrapper per ottenere MetaDati 
	 */
	
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int getTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
	public String nativeSQL(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void setReadOnly(boolean readOnly) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setTransactionIsolation(int level) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub

	}

	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
