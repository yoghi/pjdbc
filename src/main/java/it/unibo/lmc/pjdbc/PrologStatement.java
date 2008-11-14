package it.unibo.lmc.pjdbc;

import it.unibo.lmc.pjdbc.core.request.ParsedRequest;
import it.unibo.lmc.pjdbc.core.schema.TableField;
import it.unibo.lmc.pjdbc.parser.ParseException;
import it.unibo.lmc.pjdbc.parser.Psql;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

public class PrologStatement implements Statement {

	private PrologConnection conn = null;
	private Prolog dbengine = null;
	private Psql parse = null;
	private StringReader currentQuery = null;
	
	public PrologStatement(PrologConnection connection,Theory db) {
		this.conn = connection;
		this.dbengine = db;
		this.currentQuery = new StringReader("");
		this.parse = new Psql(this.currentQuery);
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
		
		Logger.getLogger("it.unibo.lmc.pjdbc").debug("eseguo query: \""+sql+"\"");
		
		this.currentQuery = new StringReader(sql);
		
		this.parse.ReInit(this.currentQuery);
		
		ParsedRequest pRequest = null;
		
		try {
			pRequest = parse.parseIt();	//TODO: su quale schema/db eseguo la query??
		} catch (ParseException e) {
			Logger.getLogger(PrologStatement.class).error(e.getLocalizedMessage());
			throw new SQLException(e.getMessage());
		}
		
		//FIXME: miglioare il controllo se è una select o meno...  
//		if ( PrologRequestType.READ != pRequest.getType() ) throw new SQLException("Not Select Statement");
		
		// Verifico se c'è un JOIN
		if ( pRequest.getNumTable() > 1 ) {
			throw new SQLException("JOIN Not implement yet");
		} else {

			//creo la richiesta prolog
			String req = pRequest.getTableNameByPosition(0)+"(";
			
			ArrayList<TableField> requestField = pRequest.getTableField(0);
			
			PrologMetaData pMeta = (PrologMetaData) this.conn.getMetaData();
			
			//se ho i metadati allora considero il numero reale di campi
			if ( null != pMeta ) {
			
				PrologResultSet columnField = (PrologResultSet) pMeta.getColumns(pRequest.getTableNameByPosition(0), null, pRequest.getTableNameByPosition(0), null);
				
				//quante colonne ho trovato
				int max = columnField.getFetchSize();
				
				columnField.first();				
				
				while(columnField.next()){
					
					System.out.println(""+columnField.getString(2)+" "+columnField.getString(3)+" "+columnField.getInt(16));
					
					TableField x = new TableField(columnField.getString(3));
					x.setTableName(columnField.getString(2));
					x.setPositionInTable(columnField.getInt(16));
					
					if ( requestField.contains(x) ) System.out.println("c'e");
				
				}
				
				
			
			} else {	//provo con i campi che mi ha dato, magari bastano
				
				for (int j = 0; j < requestField.size()-1; j++) {
					req += "X"+j+",";
				}
				
				req += "X"+(requestField.size()-1)+").";
				
			}

			System.out.println(req);
			
		}
		
		return null;
	}

	public int executeUpdate(String sql) throws SQLException {
		
		return 0;
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
		
		return null;
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
