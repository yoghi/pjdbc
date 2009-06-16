package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.core.IDatabase;
import it.unibo.lmc.pjdbc.core.database.PSchema;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;
import it.unibo.lmc.pjdbc.parser.ParseException;
import it.unibo.lmc.pjdbc.parser.Psql;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class PrologStatement implements Statement {

	private PrologConnection conn = null;
	private PSchema currentSchema = null;
	
	private Psql parse = null;
	private StringReader currentQuery = null;
	
	private Logger log;
	
	
	
	public PrologStatement(PrologConnection connection,PSchema database) {
		this.conn = connection;
		this.currentSchema = database;
		log = Logger.getLogger(PrologStatement.class);
	}

	public void addBatch(String sql) throws SQLException {
		
		
	}

	public void cancel() throws SQLException {
		
		
	}

	public void clearBatch() throws SQLException {
		
		
	}

	public void clearWarnings() throws SQLException {
		
		
	}

	public void close() throws SQLException {
		
		
	}

	public boolean execute(String sql) throws SQLException {
		
		return false;
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return false;
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		
		return false;
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		
		return false;
	}

	public int[] executeBatch() throws SQLException {
		
		return null;
	}

	/**
	 * Select type query
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		
		log.info("query: \""+sql+"\"");
		
		this.currentQuery = new StringReader(sql);
		
		if ( null == this.parse ) {
			this.parse = new Psql(this.currentQuery);
		} else {
			this.parse.ReInit(this.currentQuery);
		}
		
		ParsedCommand pRequest = null;
		
		try {
			pRequest = parse.parseIt("");	//TODO: su quale schema/db eseguo la query??
		} catch (ParseException e) {
			Logger.getLogger(PrologStatement.class).error(e.getLocalizedMessage());
			throw new SQLException(e.getMessage());
		}
		
		log.debug(pRequest.toString());
		
		if ( pRequest instanceof Select ) return this.currentSchema.applyCommand((Select)pRequest);
		
		else throw new SQLException("Invalid Command: "+pRequest.toString());
	}

	public int executeUpdate(String sql) throws SQLException {

		log.info("query: \""+sql+"\"");
		
		this.currentQuery = new StringReader(sql);
		
		if ( null == this.parse ) {
			this.parse = new Psql(this.currentQuery);
		} else {
			this.parse.ReInit(this.currentQuery);
		}
		
		ParsedCommand pRequest = null;
		
		//TODO: ... da verificare .. con il codice della Select
		
		try {
			pRequest = parse.parseIt("");	//TODO: su quale schema/db eseguo la query??
		} catch (ParseException e) {
			Logger.getLogger(PrologStatement.class).error(e.getLocalizedMessage());
			throw new SQLException(e.getMessage());
		}
		
		if ( pRequest instanceof Insert ) return this.currentSchema.applyCommand((Insert)pRequest);
		
		else if ( pRequest instanceof Update ) return this.currentSchema.applyCommand((Update)pRequest);
		
		else if ( pRequest instanceof Delete ) return this.currentSchema.applyCommand((Delete)pRequest);
		
		else throw new SQLException("Invalid Command: "+pRequest.toString());
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		
		return 0;
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		
		return 0;
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		
		return 0;
	}

	public Connection getConnection() throws SQLException {
		return this.conn;
	}

	public int getFetchDirection() throws SQLException {
		
		return 0;
	}

	public int getFetchSize() throws SQLException {
		
		return 0;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		
		return null;
	}

	public int getMaxFieldSize() throws SQLException {
		
		return 0;
	}

	public int getMaxRows() throws SQLException {
		
		return 0;
	}

	public boolean getMoreResults() throws SQLException {
		
		return false;
	}

	public boolean getMoreResults(int current) throws SQLException {
		
		return false;
	}

	public int getQueryTimeout() throws SQLException {
		
		return 0;
	}

	public ResultSet getResultSet() throws SQLException {
		
		return null;
	}

	public int getResultSetConcurrency() throws SQLException {
		
		return 0;
	}

	public int getResultSetHoldability() throws SQLException {
		
		return 0;
	}

	public int getResultSetType() throws SQLException {
		
		return 0;
	}

	public int getUpdateCount() throws SQLException {
		
		return 0;
	}

	public SQLWarning getWarnings() throws SQLException {
		
		return null;
	}

	public boolean isClosed() throws SQLException {
		
		return false;
	}

	public boolean isPoolable() throws SQLException {
		
		return false;
	}

	public void setCursorName(String name) throws SQLException {
		
		
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		
		
	}

	public void setFetchDirection(int direction) throws SQLException {
		
		
	}

	public void setFetchSize(int rows) throws SQLException {
		
		
	}

	public void setMaxFieldSize(int max) throws SQLException {
		
		
	}

	public void setMaxRows(int max) throws SQLException {
		
		
	}

	public void setPoolable(boolean poolable) throws SQLException {
		
		
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		
		
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		
		return null;
	}

}
