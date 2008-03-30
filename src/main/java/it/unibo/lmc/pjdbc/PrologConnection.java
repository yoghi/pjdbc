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
import org.apache.log4j.PropertyConfigurator;

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

	/**
	 * Costruttore 
	 * @param url url passato al factory
	 * @param filename nome del file del database
	 * @throws SQLException ...
	 * TODO gestire le ecezzioni 
	 */
	public PrologConnection(String url, String filename) throws SQLException {

		try {
			
			properties = new Properties();
			
			File file = new File(filename);
			
			boolean exists = file.exists();
		    boolean exists_prop = (new File(filename+".properties")).exists();
		    
		    if ( exists ) {
		    	String userDir = System.getProperty("user.dir");
		    	if ( !(new File(userDir+File.separator+filename)).exists() ){
		    		throw new FileNotFoundException("File "+filename+" and "+userDir+File.separator+filename+" doesn't exist");
		    	} else {
		    		filename = userDir + File.separator + filename;
		    	}
		    } else {
		    	System.out.println("Impossibile caricare il db: "+file.getAbsolutePath());
		    	boolean success = file.createNewFile();
		    	if ( success ) System.out.println("Creato database vuoto: "+file.getAbsolutePath());
		    	else System.out.println("Impossibile creare: "+file.getAbsolutePath());
		    }
		    
		    // carico eventuali opzioni
		    if ( exists_prop ) {
		    	properties.load(new FileInputStream(filename+".properties"));
		    }
		    
			this.logger_init();
			
			this.log.info("Avvio prolog db engine");

			this.dbengine = new Prolog();
			Theory t = new Theory(new FileInputStream(filename));
			
			this.log.info("Setto la teoria prolog");
			this.dbengine.setTheory(t);

		} catch (InvalidTheoryException e) {
			throw new SQLException("Prolog database internal error : "+ e.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.url = url;
	}

	protected void logger_init() {

		PropertyConfigurator.configure(properties);
		log = Logger.getLogger("PrologConnection");
		
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

	
	public Statement createStatement() throws SQLException {
		return new PrologStatement(this, this.dbengine);
	}

	
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return null;
	}

	
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return null;
	}

	
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		
		return null;
	}

	
	public boolean getAutoCommit() throws SQLException {
		
		return false;
	}

	
	public String getCatalog() throws SQLException {
		
		return null;
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

	/* (non javadoc)
	 * Restituisce un wrapper per ottenere MetaDati 
	 */
	
	public DatabaseMetaData getMetaData() throws SQLException {
		
		return null;
	}

	
	public int getTransactionIsolation() throws SQLException {
		
		return 0;
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
		

	}

	
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		

	}

	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		return false;
	}

	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

}
