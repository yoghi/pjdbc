package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.core.dml.PRequest;

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
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Var;
import alice.tuprologx.runtime.corba.SolveInfoHolder;

public class PrologResultSet implements ResultSet {

	
	
	private int currentPosition = -1;
	private int insertPosition = 0;
	
	private List<SolveInfo> row_data;
	
	private UUID code = UUID.randomUUID();
	private Logger log;
	
	private PRequest pRequest;
	
	
	/**
	 * Costrauttore
	 * @param solutions le soluzioni da usare
	 */
	public PrologResultSet(List<SolveInfo> solutions) {
		if ( null != solutions ) this.row_data = solutions;
		else this.row_data = new LinkedList<SolveInfo>();
		
		log = Logger.getLogger(PrologResultSet.class.toString()+"."+this.code);
	}
	
	public PrologResultSet(List<SolveInfo> solutions,PRequest reqs){
		this(solutions);
		this.pRequest = reqs;
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

	
	public void clearWarnings() throws SQLException {
		
		
	}

	
	public void close() throws SQLException {
		
		
	}

	
	public void deleteRow() throws SQLException {
		
		
	}

	
	public int findColumn(String columnLabel) throws SQLException {
		
		return 0;
	}

	
	/**
	 * Torno al primo elemento
	 */
	public boolean first() throws SQLException {
		this.currentPosition = -1;
		return this.row_data.size() > 0;
	}

	
	public Array getArray(int columnIndex) throws SQLException {
		return null;
	}

	
	public Array getArray(String columnLabel) throws SQLException {
		
		return null;
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

	
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		
		return null;
	}

	
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		
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

	
	public byte getByte(int columnIndex) throws SQLException {
		
		return 0;
	}

	
	public byte getByte(String columnLabel) throws SQLException {
		
		return 0;
	}

	
	public byte[] getBytes(int columnIndex) throws SQLException {
		
		return null;
	}

	
	public byte[] getBytes(String columnLabel) throws SQLException {
		
		return null;
	}

	
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		
		//new CharArrayReader(char[] buf);
		
		return null;
	}

	
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		
		//new CharArrayReader(char[] buf);
		
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

	
	public String getCursorName() throws SQLException {
		throw new SQLException("Non Implemented Yet");
	}

	
	public Date getDate(int columnIndex) throws SQLException {
		
		return null;
	}

	
	public Date getDate(String columnLabel) throws SQLException {
		
		return null;
	}

	
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		
		return null;
	}

	
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		
		return null;
	}

	
	public double getDouble(int columnIndex) throws SQLException {
		
		return 0;
	}

	
	public double getDouble(String columnLabel) throws SQLException {
		
		return 0;
	}

	
	public int getFetchDirection() throws SQLException {
		
		return 0;
	}

	
	/**
	 * Quanti elementi ci sono nel ResultSet
	 */
	public int getFetchSize() throws SQLException {
		// devo ricordarmi di togliere la riga che uso per gli inserimenti
		return this.row_data.size()-1;
	}
	
	protected String getValue(int columnIndex) throws SQLException{
		SolveInfo info = this.row_data.get(this.currentPosition);
		
		try {
		
			List<Var> results = info.getBindingVars();
			
			if ( columnIndex > results.size() ) {
				throw new SQLException("Column "+columnIndex+" not valid");
			}
			
			Var vresult = results.get(columnIndex-1);
			
			return vresult.getTerm().toString();
			
			//if ( vresult.getTerm() instanceof alice.tuprolog.Number ) 
			
		} catch (NoSolutionException e) {
			throw new SQLException("Column "+columnIndex+"not exist","SQLSTATE");
		}
	}
	
	protected String getValue(String columnLabel) throws SQLException {
		
		if ( null == columnLabel ) throw new SQLException("columLabel cann't nullable ");
		
		SolveInfo info = this.row_data.get(this.currentPosition);
		
		try {
		
			String prologLabel = null;
			if ( this.pRequest != null )
				prologLabel = this.pRequest.getVarAliasSqltoProlog(columnLabel); 
			
			if ( null == prologLabel ) prologLabel = columnLabel;

			Term value = info.getVarValue(prologLabel);

			if ( null == value ) throw new SQLException("Column "+columnLabel+" not exist","SQLSTATE");
			
			return value.toString();

		} catch (NoSolutionException e) {
			throw new SQLException("Column "+columnLabel+"not exist","SQLSTATE");
		}
	
	}

	
	public float getFloat(int columnIndex) throws SQLException {
		try {
			return Float.parseFloat( this.getValue(columnIndex) );
		} catch(NumberFormatException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		}
	}

	
	public float getFloat(String columnLabel) throws SQLException {
		try {
			return Float.parseFloat( this.getValue(columnLabel) );
		} catch(NumberFormatException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		}	
	}

	
	public int getHoldability() throws SQLException {
		
		return 0;
	}

	public int getInt(int columnIndex) throws SQLException {
		try {
			return Integer.parseInt(this.getValue(columnIndex));
		} catch(NumberFormatException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		}
	}

	
	public int getInt(String columnLabel) throws SQLException {
		try {
			return Integer.parseInt(this.getValue(columnLabel));
		} catch(NumberFormatException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		}
	}

	
	public long getLong(int columnIndex) throws SQLException {
		
		return 0;
	}

	
	public long getLong(String columnLabel) throws SQLException {
		
		return 0;
	}

	
	public ResultSetMetaData getMetaData() throws SQLException {
		
		return null;
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

	
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		
		return null;
	}

	
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		
		return null;
	}

	
	public Ref getRef(int columnIndex) throws SQLException {
		
		return null;
	}

	
	public Ref getRef(String columnLabel) throws SQLException {
		
		return null;
	}

	
	public int getRow() throws SQLException {
		
		return 0;
	}
	
	public short getShort(int columnIndex) throws SQLException {
		
		return 0;
	}

	
	public short getShort(String columnLabel) throws SQLException {
		
		return 0;
	}

	
	public Statement getStatement() throws SQLException {
		
		return null;
	}

	
	public String getString(int columnIndex) throws SQLException {
		return this.getValue(columnIndex);
	}

	
	public String getString(String columnLabel) throws SQLException {
		return this.getValue(columnLabel);
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

	
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		
		return null;
	}

	
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		
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
		//if ( )
		this.insertPosition++;
	}

	
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

	
	public void moveToCurrentRow() throws SQLException {
		
		
	}

	
	public void moveToInsertRow() throws SQLException {
		//this.row_data.add(this.insertPosition, new ArrayList<Object>());
		// devo creare un nuovo solveinfo???
		this.currentPosition = this.insertPosition;
	}

	
	public boolean next() throws SQLException {
		boolean r = this.currentPosition < this.row_data.size()-1;
		this.currentPosition++;
		return r;
	}

	
	public boolean previous() throws SQLException {
		
		return false;
	}

	
	public void refreshRow() throws SQLException {
		
		
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

	
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		
		
	}

	
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		
		
	}

	
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		
		
	}

	
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		
		
	}

	
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		
		
	}

	
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		
		
	}

	
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		
		
	}

	
	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		
		
	}

	
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		
		
	}

	
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		
		
	}

	
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		
		
	}

	
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		
		
	}

	
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		
		
	}

	
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		
		
	}

	
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		
		
	}

	
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		
		
	}

	
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		
		
	}

	
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		
		
	}

	
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		
		
	}

	
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		
		
	}

	
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		
		
	}

	
	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		
		
	}

	
	public void updateByte(int columnIndex, byte x) throws SQLException {
		
		
	}

	
	public void updateByte(String columnLabel, byte x) throws SQLException {
		
		
	}

	
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		
		
	}

	
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		
		
	}

	
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		
		
	}

	
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		
		
	}

	
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		
		
	}

	
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		
		
	}

	
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		
		
	}

	
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		
		
	}

	
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		
		
	}

	
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		
		
	}

	
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		
		
	}

	
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		
		
	}

	
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		
		
	}

	
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		
		
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
//		ArrayList<Object> f = this.row_data.get(this.currentPosition);
//		if ( columnIndex < f.size() ) f.set(columnIndex,x);
//		else f.add(columnIndex,x);
	}

	
	public void updateInt(String columnLabel, int x) throws SQLException {
		
		
	}

	
	public void updateLong(int columnIndex, long x) throws SQLException {
		
		
	}

	
	public void updateLong(String columnLabel, long x) throws SQLException {
		
		
	}

	
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		
		
	}

	
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		
		
	}

	
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		
		
	}

	
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		
		
	}

	
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		
		
	}

	
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		
		
	}

	
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		
		
	}

	
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		
		
	}

	
	public void updateNString(int columnIndex, String string)
			throws SQLException {
		
		
	}

	
	public void updateNString(String columnLabel, String string)
			throws SQLException {
		
		
	}

	
	public void updateNull(int columnIndex) throws SQLException {
		
		
	}

	
	public void updateNull(String columnLabel) throws SQLException {
		
		
	}

	
	public void updateObject(int columnIndex, Object x) throws SQLException {
//		ArrayList<Object> f = this.row_data.get(this.currentPosition);
//		f.add(columnIndex,x);
	}

	
	public void updateObject(String columnLabel, Object x) throws SQLException {
		
		
	}

	
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
			throws SQLException {
		
		
	}

	
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
			throws SQLException {
		
		
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

	
	public void updateString(int columnIndex, String x) throws SQLException {
//		ArrayList<Object> f = this.row_data.get(this.currentPosition);
//		if (columnIndex < f.size()) f.set(columnIndex,x); 
//		else f.add(columnIndex,x);
	}

	
	public void updateString(String columnLabel, String x) throws SQLException {
		
		
	}

	
	public void updateTime(int columnIndex, Time x) throws SQLException {
		
		
	}

	
	public void updateTime(String columnLabel, Time x) throws SQLException {
		
		
	}

	
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		
		
	}

	
	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		
		
	}

	
	public boolean wasNull() throws SQLException {
		
		return false;
	}

	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		return false;
	}

	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return null;
	}

}
