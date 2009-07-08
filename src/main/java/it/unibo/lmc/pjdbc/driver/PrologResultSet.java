package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.udt.PArray;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import alice.tuprolog.Term;

public class PrologResultSet implements ResultSet {

	private int insertPosition = 0;
	
	private PResultSet pResult = null;
	private PrologDatabase db;

	private String requestSql;
	private Statement statement;

	public PrologResultSet(String sql, PResultSet result, PrologDatabase currentDatabase, Statement stmt) {
		this.pResult = result;
		this.db = currentDatabase;
		this.requestSql = sql;
		this.statement = stmt;
	}


	public void clearWarnings() throws PSQLException {

	}

	public void close() throws PSQLException {
		
	}

	public int findColumn(String columnLabel) throws PSQLException {
		return this.pResult.findColumn(columnLabel);
	}

	/**
	 * Torno al primo elemento
	 */
	public boolean first() throws PSQLException {
		return this.pResult.first();
	}

	
	public void moveToCurrentRow() throws PSQLException {}

	public void moveToInsertRow() throws PSQLException {
		
//		this.pResult.getFields(); .. divido per tabella
//		creo Term[] appropriati e poi uso set campi appropriati where camp
//		this.db.executeUpdate("INSERT into ... VALUE ...  ");
		
	}

	public boolean next() throws PSQLException {
		return this.pResult.next();
	}

	public boolean previous() throws PSQLException {
		return this.pResult.previous();
	}

	public void refreshRow() throws PSQLException {
		PResultSet rs = this.db.executeSelect(this.requestSql);
		this.pResult = rs;
	}

	public byte getByte(int columnIndex) throws PSQLException {
		Term value = this.pResult.getValue(columnIndex);
		try {
			return Byte.parseByte(value.toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public byte getByte(String columnLabel) throws PSQLException {
		Term value = this.pResult.getValue(columnLabel);
		try {
			return Byte.parseByte(value.toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public byte[] getBytes(int columnIndex) throws SQLException {

		Array value = this.getArray(columnIndex);
		try {
			
			Object array = value.getArray();
			
			if ( array instanceof ArrayList ) {
				Object[] h = ((ArrayList)array).toArray();
				// dalle stringhe estrapolo i byte con Byte.parseByte(..);
			}
			
			//TODO bisogna vedere se uno dei campi della clausola è una lista come viene restituita
			
		} catch (NumberFormatException e) {
			throw new SQLException(e.getLocalizedMessage(), "SQLSTATE");
		}
		
		return null;
	}

	public byte[] getBytes(String columnLabel) throws SQLException {

		// VEDI SOPRA!!
		return null;
	}

	public String getCursorName() throws SQLException {
		throw new SQLException("Non Implemented Yet");
	}

	public Date getDate(int columnIndex) throws SQLException {
		Term value = this.pResult.getValue(columnIndex);
		try {
			return Date.valueOf(value.toString());	//s a String object representing a date in in the format "yyyy-mm-dd"
		} catch (IllegalArgumentException e) {
			throw new SQLException(e.getLocalizedMessage(), "SQLSTATE");
		}
	}

	public Date getDate(String columnLabel) throws SQLException {
		Term value = this.pResult.getValue(columnLabel);
		try {
			return Date.valueOf(value.toString());	//s a String object representing a date in in the format "yyyy-mm-dd"
		} catch (IllegalArgumentException e) {
			throw new SQLException(e.getLocalizedMessage(), "SQLSTATE");
		}
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {

		/** MMMM NON HO CAPITO COME FUNZIA LA QUESTIONE di CALENDAR come lo uso??? */
		
		return null;
	}

	public Date getDate(String columnLabel, Calendar cal) throws SQLException {

		/** MMMM NON HO CAPITO COME FUNZIA LA QUESTIONE di CALENDAR come lo uso??? */
		
		return null;
	}

	public double getDouble(int columnIndex) throws SQLException {
		Term value = this.pResult.getValue(columnIndex);
		try {
			return Double.parseDouble(value.toString());
		} catch (NumberFormatException e) {
			throw new SQLException("problema nella conversione : "+e.getLocalizedMessage(), "SQLSTATE");
		}
	}

	public double getDouble(String columnLabel) throws SQLException {
		Term value = this.pResult.getValue(columnLabel);
		try {
			return Double.parseDouble(value.toString());
		} catch (NumberFormatException e) {
			throw new SQLException("problema nella conversione : "+e.getLocalizedMessage(), "SQLSTATE");
		}
	}

	/**
	 * Quanti elementi ci sono nel ResultSet
	 */
	public int getFetchSize() throws SQLException {
		return this.pResult.getFetchSize();
	}
	
	public Array getArray(int columnIndex) throws PSQLException {
		
		/** NOTA BENE ARRAY è una classe java.sql dalla quale poi estrai gli elementi dell'array */
		
		Term t = this.pResult.getValue(columnIndex);
		
		if ( !t.isList()) {
			throw new PSQLException("Column " + columnIndex + " not valid array", PSQLState.DATA_TYPE_MISMATCH);
		}
		
		return new PArray(t); 
		
		
	}

	public Array getArray(String columnLabel) throws SQLException {

		/** NOTA BENE ARRAY è una classe java.sql dalla quale poi estrai gli elementi dell'array */
		
		Term t = this.pResult.getValue(columnLabel);
		
		if ( !t.isList()) {
			throw new PSQLException("Column " + columnLabel + " not valid array", PSQLState.DATA_TYPE_MISMATCH);
		}
		
		return new PArray(t); 
	}

	public float getFloat(int columnIndex) throws SQLException {
		try {
			return Float.parseFloat(this.pResult.getValue(columnIndex).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public float getFloat(String columnLabel) throws SQLException {
		try {
			return Float.parseFloat(this.pResult.getValue(columnLabel).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public int getInt(int columnIndex) throws SQLException {
		try {
			return Integer.parseInt(this.pResult.getValue(columnIndex).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public int getInt(String columnLabel) throws SQLException {
		try {
			return Integer.parseInt(this.pResult.getValue(columnLabel).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public long getLong(int columnIndex) throws SQLException {
		try {
			return Long.parseLong(this.pResult.getValue(columnIndex).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public long getLong(String columnLabel) throws SQLException {
		try {
			return Long.parseLong(this.pResult.getValue(columnLabel).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	/**
	 * Restituisco i metadati sui campi del resultset
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return new PrologResultSetMetaData( this.db , this.pResult );
	}

	public int getRow() throws SQLException {

		return 0;
	}

	public short getShort(int columnIndex) throws SQLException {
		try {
			return Short.parseShort(this.pResult.getValue(columnIndex).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public short getShort(String columnLabel) throws SQLException {
		try {
			return Short.parseShort(this.pResult.getValue(columnLabel).toString());
		} catch (NumberFormatException e) {
			throw new PSQLException("problema nella conversione : "+e.getLocalizedMessage(), PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	public Statement getStatement() throws SQLException {
		return this.statement;
	}

	public String getString(int columnIndex) throws SQLException {
		return this.pResult.getValue(columnIndex).toString();
	}

	public String getString(String columnLabel) throws SQLException {
		return this.pResult.getValue(columnLabel).toString();
	}

	public Time getTime(int columnIndex) throws SQLException {

		return null;
	}

	public Time getTime(String columnLabel) throws SQLException {

		return null;
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {

		return null;
	}

	public Time getTime(String columnLabel, Calendar cal) throws SQLException {

		return null;
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {

		return null;
	}

	public Timestamp getTimestamp(String columnLabel) throws SQLException {

		return null;
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {

		return null;
	}

	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {

		return null;
	}

	public int getType() throws SQLException {

		return 0;
	}

	public URL getURL(int columnIndex) throws SQLException {

		return null;
	}

	public URL getURL(String columnLabel) throws SQLException {

		return null;
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {

		return null;
	}

	public InputStream getUnicodeStream(String columnLabel) throws SQLException {

		return null;
	}

	public SQLWarning getWarnings() throws SQLException {

		return null;
	}

	/**
	 * @todo aggiorno il db se questo resultset ne è legato
	 */
	public void insertRow() throws SQLException {
		// if ( )
		this.insertPosition++;
	}

	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 *	UN GIORNO QUELLE SOTTO LE IMPLEMENTERO!
	 *	MA NON OGGI!!!! 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	public boolean isAfterLast() throws SQLException {

		return false;
	}

	public boolean isBeforeFirst() throws SQLException {

		return false;
	}

	public boolean isClosed() throws SQLException {

		return false;
	}

	public boolean isFirst() throws SQLException {

		return false;
	}

	public boolean isLast() throws SQLException {

		return false;
	}

	public boolean last() throws SQLException {

		return false;
	}

	public boolean relative(int rows) throws SQLException {

		return false;
	}

	public boolean rowDeleted() throws SQLException {

		return false;
	}

	public boolean rowInserted() throws SQLException {

		return false;
	}

	public boolean rowUpdated() throws SQLException {

		return false;
	}

	public void setFetchDirection(int direction) throws SQLException {

	}

	public void setFetchSize(int rows) throws SQLException {

	}

	public void updateArray(int columnIndex, Array x) throws SQLException {

	}

	public void updateArray(String columnLabel, Array x) throws SQLException {

	}

	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {

	}

	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {

	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {

	}

	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {

	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {

	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {

	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {

	}

	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {

	}

	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {

	}

	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {

	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {

	}

	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {

	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {

	}

	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {

	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {

	}

	public void updateBlob(String columnLabel, Blob x) throws SQLException {

	}

	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {

	}

	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {

	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {

	}

	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {

	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {

	}

	public void updateBoolean(String columnLabel, boolean x) throws SQLException {

	}

	public void updateByte(int columnIndex, byte x) throws SQLException {

	}

	public void updateByte(String columnLabel, byte x) throws SQLException {

	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {

	}

	public void updateBytes(String columnLabel, byte[] x) throws SQLException {

	}

	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {

	}

	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {

	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {

	}

	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {

	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

	}

	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {

	}

	public void updateClob(String columnLabel, Clob x) throws SQLException {

	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {

	}

	public void updateClob(String columnLabel, Reader reader) throws SQLException {

	}

	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {

	}

	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateDate(int columnIndex, Date x) throws SQLException {

	}

	public void updateDate(String columnLabel, Date x) throws SQLException {

	}

	public void updateDouble(int columnIndex, double x) throws SQLException {

	}

	public void updateDouble(String columnLabel, double x) throws SQLException {

	}

	public void updateFloat(int columnIndex, float x) throws SQLException {

	}

	public void updateFloat(String columnLabel, float x) throws SQLException {

	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		// ArrayList<Object> f = this.row_data.get(this.currentPosition);
		// if ( columnIndex < f.size() ) f.set(columnIndex,x);
		// else f.add(columnIndex,x);
	}

	public void updateInt(String columnLabel, int x) throws SQLException {

	}

	public void updateLong(int columnIndex, long x) throws SQLException {

	}

	public void updateLong(String columnLabel, long x) throws SQLException {

	}

	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {

	}

	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {

	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {

	}

	public void updateNClob(String columnLabel, Reader reader) throws SQLException {

	}

	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {

	}

	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateNString(int columnIndex, String string) throws SQLException {

	}

	public void updateNString(String columnLabel, String string) throws SQLException {

	}

	public void updateNull(int columnIndex) throws SQLException {

	}

	public void updateNull(String columnLabel) throws SQLException {

	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		// ArrayList<Object> f = this.row_data.get(this.currentPosition);
		// f.add(columnIndex,x);
	}

	public void updateObject(String columnLabel, Object x) throws SQLException {
		
	}

	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {

	}

	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {

	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {

	}

	public void updateRef(String columnLabel, Ref x) throws SQLException {

	}

	public void updateRow() throws SQLException {

	}

	public void updateShort(int columnIndex, short x) throws SQLException {

	}

	public void updateShort(String columnLabel, short x) throws SQLException {

	}

	public boolean absolute(int row) throws SQLException {

		return false;
	}

	public void afterLast() throws SQLException {

	}

	public void beforeFirst() throws SQLException {

	}

	public void cancelRowUpdates() throws SQLException {

	}

	public void updateString(int columnIndex, String x) throws SQLException {
		// ArrayList<Object> f = this.row_data.get(this.currentPosition);
		// if (columnIndex < f.size()) f.set(columnIndex,x);
		// else f.add(columnIndex,x);
	}

	public void updateString(String columnLabel, String x) throws SQLException {
		
//		PSolution info = this.rowData.get(this.currentPosition);
//		
//		MColumn minfo = this.mfields.get(columnLabel);
//		
//		info.setVar(columnLabel,x);	//TODO da pensare...
		
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		
	}

	public void updateTime(String columnLabel, Time x) throws SQLException {

	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {

	}

	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {

	}

	public boolean wasNull() throws SQLException {

		return false;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {

		return false;
	}
	
	public void deleteRow() throws SQLException {

	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {

		return null;
	}

	public InputStream getAsciiStream(String columnLabel) throws SQLException {

		return null;
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {

		return null;
	}

	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {

		return null;
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {

		return null;
	}

	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {

		return null;
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {

		return null;
	}

	public InputStream getBinaryStream(String columnLabel) throws SQLException {

		return null;
	}

	public Blob getBlob(int columnIndex) throws SQLException {

		return null;
	}

	public Blob getBlob(String columnLabel) throws SQLException {

		return null;
	}

	public boolean getBoolean(int columnIndex) throws SQLException {

		return false;
	}

	public boolean getBoolean(String columnLabel) throws SQLException {

		return false;
	}
	
	public Reader getCharacterStream(int columnIndex) throws SQLException {

		// new CharArrayReader(char[] buf);

		return null;
	}

	public Reader getCharacterStream(String columnLabel) throws SQLException {

		// new CharArrayReader(char[] buf);

		return null;
	}

	public Clob getClob(int columnIndex) throws SQLException {

		return null;
	}

	public Clob getClob(String columnLabel) throws SQLException {

		return null;
	}

	public int getConcurrency() throws SQLException {

		return 0;
	}
	
	public int getFetchDirection() throws SQLException {

		return 0;
	}

	public int getHoldability() throws SQLException {

		return 0;
	}
	
	public Reader getNCharacterStream(int columnIndex) throws SQLException {

		return null;
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {

		return null;
	}

	public String getNString(int columnIndex) throws SQLException {

		return null;
	}

	public String getNString(String columnLabel) throws SQLException {

		return null;
	}

	public Object getObject(int columnIndex) throws SQLException {

		return null;
	}

	public Object getObject(String columnLabel) throws SQLException {

		return null;
	}

	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {

		return null;
	}

	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {

		return null;
	}

	public Ref getRef(int columnIndex) throws SQLException {

		return null;
	}

	public Ref getRef(String columnLabel) throws SQLException {

		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {

		return null;
	}

}
