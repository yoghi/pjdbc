package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.meta.MCatalog;
import it.unibo.lmc.pjdbc.database.meta.MColumn;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrologResultSetMetaData implements ResultSetMetaData {

	
	private List<MColumn> fieldInfo = new ArrayList<MColumn>();
	private MCatalog catalog;

	/**
	 * Costruttore
	 * @param db
	 * @param pResult
	 */
	public PrologResultSetMetaData(PrologDatabase db, PResultSet pResult) {
		
		List<TableField> fields = pResult.getFields();
		
		this.catalog = db.getCatalogInfo();
		
		for (TableField tableField : fields) {
		
			try {
			
				String schemaName;
				
				if ( tableField.getSchema() == null ){
					schemaName = db.getCurrentSchema();
				} else {
					schemaName = tableField.getSchema();
				}
				
				MColumn column = catalog.getMetaSchemaFromName(schemaName).getMetaTableInfo(tableField.getTableName()).getColumnMeta(tableField.getColumnName());
				
				this.fieldInfo.add(column);
			
			} catch (PSQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Restituisce il nome del catalog corrente
	 * @param column
	 */
	public String getCatalogName(int column) throws SQLException {
		// TODO essendo un solo catalog possibile
		return this.catalog.getName();
	}

	public String getColumnClassName(int column) throws SQLException {
		MColumn c = this.fieldInfo.get(column);
		return c.getColumnType().name();
	}

	public int getColumnCount() throws SQLException {
		return this.fieldInfo.size();
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public String getColumnLabel(int column) throws SQLException {
		return this.getColumnClassName(column);
	}

	public String getColumnName(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column).getColumnName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public int getColumnType(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column).getColumnType().getSqlType();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public String getColumnTypeName(int column) throws SQLException {
		return this.fieldInfo.get(column).getColumnType().name();
	}

	public int getPrecision(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public int getScale(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public String getSchemaName(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column).getSchemaName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public String getTableName(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column).getTableName();
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
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public int isNullable(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean isReadOnly(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean isSearchable(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean isSigned(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean isWritable(int column) throws SQLException {
		throw new PSQLException("non implemented", PSQLState.NOT_IMPLEMENTED);
	}

}
