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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

public class PrologConnection implements Connection {

	/**
	 * Prolog Engine
	 */
	private Prolog dbengine = null;
	
	/**
	 * Metadati Database
	 */
	private PrologMetaData databaseMetaData = null;
	
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
			
			this.log.debug("Avvio prolog db engine");

			this.dbengine = new Prolog();
			Theory t = new Theory(new FileInputStream(filename));
			
			this.log.debug("Setto la teoria prolog");
			this.dbengine.setTheory(t);
			
			try {
				this.databaseMetaData = new PrologMetaData(this.dbengine);
			} catch (Exception e) {
				this.log.warn(e);
				//throw new SQLException("metabase not present");
			}

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
		log = Logger.getLogger("it.unibo.lmc.pjdbc");
		
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
		

	}

	
	public void close() throws SQLException {
		

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

	/**
	 * Restituisce un wrapper per ottenere MetaDati
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.databaseMetaData;
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

}
