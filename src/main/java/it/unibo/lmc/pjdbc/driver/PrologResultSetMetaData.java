package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.core.meta.MColumn;
import it.unibo.lmc.pjdbc.core.utils.PSQLException;
import it.unibo.lmc.pjdbc.core.utils.PSQLState;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class PrologResultSetMetaData implements ResultSetMetaData {

	private List<MColumn> fileds;
	
	
	public PrologResultSetMetaData(List<MColumn> columns) {
		this.fileds = columns;
	}

	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getColumnCount() throws SQLException {
		return this.fileds.size();
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getColumnLabel(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnName(int column) throws SQLException {
		try { 
			return this.fileds.get(column).getColumnName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public int getColumnType(int column) throws SQLException {
		try { 
			return this.fileds.get(column).getColumnType().getSqlType();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSchemaName(int column) throws SQLException {
		try { 
			return this.fileds.get(column).getSchemaName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public String getTableName(int column) throws SQLException {
		try { 
			return this.fileds.get(column).getTableName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public int isNullable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isReadOnly(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSearchable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
