/**
 * 
 */
package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.core.PSchema;
import it.unibo.lmc.pjdbc.core.command.PResultSet;
import it.unibo.lmc.pjdbc.core.meta.MColumn;
import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import alice.tuprolog.Term;


public abstract class PrologMetaData implements DatabaseMetaData {

	private MSchema mschema;
	
	/**
	 * <ol>
	 * <li>TABLE_CAT String => table catalog (may be null)</li>
	 * <li>TABLE_SCHEM String => table schema (may be null) </li>
	 * <li>TABLE_NAME String => table name </li>
	 * <li>COLUMN_NAME String => column name </li>
	 * <li>DATA_TYPE int => SQL type from java.sql.Types </li>
	 * <li>TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified </li>
	 * <li>COLUMN_SIZE int => column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision. </li>
	 * <li>BUFFER_LENGTH is not used. </li>
	 * <li>DECIMAL_DIGITS int => the number of fractional digits </li>
	 * <li>NUM_PREC_RADIX int => Radix (typically either 10 or 2) </li>
	 * <li>NULLABLE int => is NULL allowed.  columnNoNulls - might not allow NULL values columnNullable - definitely allows NULL values  columnNullableUnknown -nullability unknown </li>
	 * <li>REMARKS String => comment describing column (may be null) </li>
	 * <li>COLUMN_DEF String => default value (may be null) </li>
	 * <li>SQL_DATA_TYPE int => unused </li>
	 * <li>SQL_DATETIME_SUB int => unused </li>
	 * <li>CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column </li>
	 * <li>ORDINAL_POSITION int => index of column in table (starting at 1) </li>
	 * <li>IS_NULLABLE String => "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows. </li>
	 * <li>SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) </li>
	 * <li>SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) </li>
	 * <li>SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF) </li>
	 * <li>SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)</li>
	 * </ol>
	 */
	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {

		List<TableField> fields = null;
		
		List<Term[]> rows = null;
		
		PSchema schema = null;
		
		PResultSet result = new PResultSet(fields, rows, schema);
		PrologResultSet a = new PrologResultSet(result);
		
		//schemaPattern.... serve nel caso di più schemi
		
		MTable mtable = mschema.getMetaTableInfo(tableNamePattern);
		
		//qui servirebbe qualcosa tipo LIKE... ergo regexp
		MColumn column = mtable.getColumnMeta(columnNamePattern);
		
//		//schema e catalog possono essere null, table e colum NO!
//		
//		PrologResultSet res = new PrologResultSet();
//
//		ArrayList t = (ArrayList) this.table.get(tableNamePattern);
//
//		for (int i = 0; i < t.size(); i++) {
//
//			res.moveToInsertRow();
//			TableSpecificField f = (TableSpecificField) t.get(i);
//			res.updateString(0, null);
//			res.updateString(1, null);
//			res.updateString(2, tableNamePattern);
//			res.updateString(3, f.getColumnName());
//			res.updateInt(4, f.getType());
//			res.updateObject(5, null);
//			res.updateObject(6, null);
//			res.updateObject(7, null);
//			res.updateObject(8, null);
//			res.updateObject(9, null);
//			res.updateObject(10, null);
//			res.updateObject(11, null);
//			res.updateObject(12, null);
//			res.updateObject(13, null);
//			res.updateObject(14, null);
//			res.updateObject(15, null);
//			res.updateInt(16, f.getPositionInTable());
//			res.insertRow();
//
//		}
//
//		res.moveToCurrentRow();

//		return res;
		
		return null;
	}
}
