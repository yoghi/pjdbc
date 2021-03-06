package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.meta.MColumn;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.database.utils.PTypes;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrologResultSetMetaData implements ResultSetMetaData {

	
	private List<MColumn> fieldInfo = new ArrayList<MColumn>();
	private PrologDatabase database;

	/**
	 * Costruttore
	 * @param db
	 * @param pResult
	 */
	public PrologResultSetMetaData(PrologDatabase db, PResultSet pResult) {
		
		List<TableField> fields = pResult.getFields();
		
		this.database = db;
		
		for (TableField tableField : fields) {
		
			try {
			
				String schemaName;
				
				if ( tableField.getSchema() == null ){
					schemaName = db.getCurrentSchema();
				} else {
					schemaName = tableField.getSchema();
				}
				
				MColumn column = null;
				if ( tableField.getColumnName().startsWith("$") ){
					MColumn[] columns = db.getMetaSchema(schemaName).getMetaTableInfo(tableField.getTableName()).getColumns();
					int pos = Integer.parseInt( tableField.getColumnName().replace("$", "")  );
					column = columns[pos];
				} else {
					MSchema mschema = db.getMetaSchema(schemaName);
					MTable mtable = mschema.getMetaTableInfo(tableField.getTableName());
					if ( mtable == null ){
						column = new MColumn(mschema, new MTable(mschema, "", 1) , tableField.getColumnName(), PTypes.STRING);
					} else {
						column = db.getMetaSchema(schemaName).getMetaTableInfo(tableField.getTableName()).getColumnMeta(tableField.getColumnName());
					}
					
				}
				
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
		return this.database.getCatalog().getName();
	}

	public String getColumnClassName(int column) throws SQLException {
		MColumn c = this.fieldInfo.get(column-1);
		return c.getColumnType().name();
	}

	public int getColumnCount() throws SQLException {
		return this.fieldInfo.size();
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return 255;
	}

	public String getColumnLabel(int column) throws SQLException {
		return this.fieldInfo.get(column-1).getQualifiedName();
	}

	public String getColumnName(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column-1).getColumnName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public int getColumnType(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column-1).getColumnType().getSqlType();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public String getColumnTypeName(int column) throws SQLException {
		return this.fieldInfo.get(column-1).getColumnType().name();
	}

	public int getPrecision(int column) throws SQLException {
		return 1;
	}

	public int getScale(int column) throws SQLException {
		return 1;
	}

	public String getSchemaName(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column-1).getSchemaName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public String getTableName(int column) throws SQLException {
		try { 
			return this.fieldInfo.get(column-1).getTableName();
		} catch (Exception e) {
			throw new PSQLException("column "+column+" not exist",PSQLState.UNDEFINED_COLUMN);
		}
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return true;
	}

	public boolean isCurrency(int column) throws SQLException {
		return false;
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		return false;
	}

	public int isNullable(int column) throws SQLException {
		return ResultSetMetaData.columnNullable;
	}

	public boolean isReadOnly(int column) throws SQLException {
		return false;
	}

	public boolean isSearchable(int column) throws SQLException {
		return false;
	}

	public boolean isSigned(int column) throws SQLException {
		return false;
	}

	public boolean isWritable(int column) throws SQLException {
		return false;
	}

}
