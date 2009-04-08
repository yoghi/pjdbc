/**
 * 
 */
package it.unibo.lmc.pjdbc.driver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;


public class PrologMetaData implements DatabaseMetaData {

	private Hashtable<String, ArrayList<TableSpecificField>> table = new Hashtable<String, ArrayList<TableSpecificField>>();

	/**
	 * Costruttore
	 * 
	 * @throws SQLException , non posso creare un prologMetaData
	 */
	public PrologMetaData(Theory th) {
		try {

			Prolog prolog = new Prolog();
			prolog.setTheory(th);

			SolveInfo i = prolog.solve("metabase(TABLE,POSITION,NAME,TYPE).");
			if (i.isSuccess()) {

				while (i.isSuccess()) {

					Term table_name = i.getTerm("TABLE");
					Term field_position = i.getTerm("POSITION");
					Term field_name = i.getTerm("NAME");
					Term field_type = i.getTerm("TYPE");

					if (table_name.isAtom() && field_name.isAtom() && (field_position instanceof Number) && field_type.isAtom()) {

						ArrayList<TableSpecificField> fields = null;

						if (!this.table.containsKey(table_name.toString())) {
							fields = new ArrayList<TableSpecificField>();
							this.table.put(table_name.toString(), fields);
							Logger.getLogger("it.unibo.lmc.pjdbc").debug("trovati metadati tabella " + table_name.toString());
						} else {
							fields = (ArrayList<TableSpecificField>) this.table.get(table_name.toString());
						}

						TableSpecificField f = new TableSpecificField(field_name.toString());
						f.setPositionInTable(((Number) field_position).intValue());

						if (field_type.toString().equals("int"))
							f.setType(java.sql.Types.INTEGER);
						else if (field_type.toString().equals("string"))
							f.setType(java.sql.Types.VARCHAR);

						fields.add(f);

					} else
						//throw new SQLException("Malformed metabase");
						System.out.println("Malformed metabase");

					try {
						i = prolog.solveNext();
					} catch (NoMoreSolutionException e) {
						break;
					}
				}

			} else {

				Iterator i = th.iterator(new Prolog());
				while(i.hasNext()){
					Term t = (Term)i.next();
					if ( t instanceof Struct ){
			        	
			        	Struct s = (Struct)t;
			        	
			        	// rimane il caso "predicato(...):-!"
			        	if ( s.isGround() ){
			        		//NB: X e _ sono due variabili
			        		//System.out.println("Ground (non contiene variabili) "+x.isGround());	        		
			        		int l = s.getArity();
			        		System.out.println(s.getName()+"/"+l);
			        	}
			        }
				}
				
			}

		} catch (MalformedGoalException e) {
			e.printStackTrace();
		} catch (NoSolutionException e) {
			e.printStackTrace();
		} catch (UnknownVarException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#allProceduresAreCallable()
	 */
	public boolean allProceduresAreCallable() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#allTablesAreSelectable()
	 */
	public boolean allTablesAreSelectable() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#autoCommitFailureClosesAllResultSets()
	 */
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#dataDefinitionCausesTransactionCommit()
	 */

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#dataDefinitionIgnoredInTransactions()
	 */

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#deletesAreDetected(int)
	 */

	public boolean deletesAreDetected(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#doesMaxRowSizeIncludeBlobs()
	 */

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getAttributes(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */

	public ResultSet getAttributes(String arg0, String arg1, String arg2, String arg3) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getBestRowIdentifier(java.lang.String,
	 * java.lang.String, java.lang.String, int, boolean)
	 */

	public ResultSet getBestRowIdentifier(String arg0, String arg1, String arg2, int arg3, boolean arg4) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getCatalogSeparator()
	 */

	public String getCatalogSeparator() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getCatalogTerm()
	 */

	public String getCatalogTerm() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getCatalogs()
	 */

	public ResultSet getCatalogs() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getClientInfoProperties()
	 */

	public ResultSet getClientInfoProperties() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getColumnPrivileges(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */

	public ResultSet getColumnPrivileges(String arg0, String arg1, String arg2, String arg3) throws SQLException {

		return null;
	}

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

		//schema e catalog possono essere null, table e colum NO!
		
		PrologResultSet res = new PrologResultSet();

		ArrayList t = (ArrayList) this.table.get(tableNamePattern);

		for (int i = 0; i < t.size(); i++) {

			res.moveToInsertRow();
			TableSpecificField f = (TableSpecificField) t.get(i);
			res.updateString(0, null);
			res.updateString(1, null);
			res.updateString(2, tableNamePattern);
			res.updateString(3, f.getColumnName());
			res.updateInt(4, f.getType());
			res.updateObject(5, null);
			res.updateObject(6, null);
			res.updateObject(7, null);
			res.updateObject(8, null);
			res.updateObject(9, null);
			res.updateObject(10, null);
			res.updateObject(11, null);
			res.updateObject(12, null);
			res.updateObject(13, null);
			res.updateObject(14, null);
			res.updateObject(15, null);
			res.updateInt(16, f.getPositionInTable());
			res.insertRow();

		}

		res.moveToCurrentRow();

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getConnection()
	 */

	public Connection getConnection() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getCrossReference(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */

	public ResultSet getCrossReference(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDatabaseMajorVersion()
	 */

	public int getDatabaseMajorVersion() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDatabaseMinorVersion()
	 */

	public int getDatabaseMinorVersion() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDatabaseProductName()
	 */

	public String getDatabaseProductName() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDatabaseProductVersion()
	 */

	public String getDatabaseProductVersion() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDefaultTransactionIsolation()
	 */

	public int getDefaultTransactionIsolation() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDriverMajorVersion()
	 */

	public int getDriverMajorVersion() {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDriverMinorVersion()
	 */

	public int getDriverMinorVersion() {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDriverName()
	 */

	public String getDriverName() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getDriverVersion()
	 */

	public String getDriverVersion() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getExportedKeys(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getExportedKeys(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getExtraNameCharacters()
	 */

	public String getExtraNameCharacters() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getFunctionColumns(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */

	public ResultSet getFunctionColumns(String arg0, String arg1, String arg2, String arg3) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getFunctions(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getFunctions(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getIdentifierQuoteString()
	 */

	public String getIdentifierQuoteString() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getImportedKeys(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getImportedKeys(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String,
	 * java.lang.String, java.lang.String, boolean, boolean)
	 */

	public ResultSet getIndexInfo(String arg0, String arg1, String arg2, boolean arg3, boolean arg4) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getJDBCMajorVersion()
	 */

	public int getJDBCMajorVersion() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getJDBCMinorVersion()
	 */

	public int getJDBCMinorVersion() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxBinaryLiteralLength()
	 */

	public int getMaxBinaryLiteralLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxCatalogNameLength()
	 */

	public int getMaxCatalogNameLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxCharLiteralLength()
	 */

	public int getMaxCharLiteralLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxColumnNameLength()
	 */

	public int getMaxColumnNameLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInGroupBy()
	 */

	public int getMaxColumnsInGroupBy() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInIndex()
	 */

	public int getMaxColumnsInIndex() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInOrderBy()
	 */

	public int getMaxColumnsInOrderBy() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInSelect()
	 */

	public int getMaxColumnsInSelect() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInTable()
	 */

	public int getMaxColumnsInTable() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxConnections()
	 */

	public int getMaxConnections() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxCursorNameLength()
	 */

	public int getMaxCursorNameLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxIndexLength()
	 */

	public int getMaxIndexLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxProcedureNameLength()
	 */

	public int getMaxProcedureNameLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxRowSize()
	 */

	public int getMaxRowSize() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxSchemaNameLength()
	 */

	public int getMaxSchemaNameLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxStatementLength()
	 */

	public int getMaxStatementLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxStatements()
	 */

	public int getMaxStatements() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxTableNameLength()
	 */

	public int getMaxTableNameLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxTablesInSelect()
	 */

	public int getMaxTablesInSelect() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getMaxUserNameLength()
	 */

	public int getMaxUserNameLength() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getNumericFunctions()
	 */

	public String getNumericFunctions() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getPrimaryKeys(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getPrimaryKeys(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getProcedureColumns(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */

	public ResultSet getProcedureColumns(String arg0, String arg1, String arg2, String arg3) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getProcedureTerm()
	 */

	public String getProcedureTerm() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getProcedures(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getProcedures(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getResultSetHoldability()
	 */

	public int getResultSetHoldability() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSQLKeywords()
	 */

	public String getSQLKeywords() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSQLStateType()
	 */

	public int getSQLStateType() throws SQLException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSchemaTerm()
	 */

	public String getSchemaTerm() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSchemas()
	 */

	public ResultSet getSchemas() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSchemas(java.lang.String,
	 * java.lang.String)
	 */

	public ResultSet getSchemas(String arg0, String arg1) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSearchStringEscape()
	 */

	public String getSearchStringEscape() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getStringFunctions()
	 */

	public String getStringFunctions() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSuperTables(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getSuperTables(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSuperTypes(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getSuperTypes(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getSystemFunctions()
	 */

	public String getSystemFunctions() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getTablePrivileges(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getTableTypes()
	 */

	public ResultSet getTableTypes() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getTables(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String[])
	 */

	public ResultSet getTables(String arg0, String arg1, String arg2, String[] arg3) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getTimeDateFunctions()
	 */

	public String getTimeDateFunctions() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getTypeInfo()
	 */

	public ResultSet getTypeInfo() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getUDTs(java.lang.String,
	 * java.lang.String, java.lang.String, int[])
	 */

	public ResultSet getUDTs(String arg0, String arg1, String arg2, int[] arg3) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getURL()
	 */

	public String getURL() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getUserName()
	 */

	public String getUserName() throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#getVersionColumns(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public ResultSet getVersionColumns(String arg0, String arg1, String arg2) throws SQLException {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#insertsAreDetected(int)
	 */

	public boolean insertsAreDetected(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#isCatalogAtStart()
	 */

	public boolean isCatalogAtStart() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#isReadOnly()
	 */

	public boolean isReadOnly() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#locatorsUpdateCopy()
	 */

	public boolean locatorsUpdateCopy() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#nullPlusNonNullIsNull()
	 */

	public boolean nullPlusNonNullIsNull() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#nullsAreSortedAtEnd()
	 */

	public boolean nullsAreSortedAtEnd() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#nullsAreSortedAtStart()
	 */

	public boolean nullsAreSortedAtStart() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#nullsAreSortedHigh()
	 */

	public boolean nullsAreSortedHigh() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#nullsAreSortedLow()
	 */

	public boolean nullsAreSortedLow() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#othersDeletesAreVisible(int)
	 */

	public boolean othersDeletesAreVisible(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#othersInsertsAreVisible(int)
	 */

	public boolean othersInsertsAreVisible(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#othersUpdatesAreVisible(int)
	 */

	public boolean othersUpdatesAreVisible(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#ownDeletesAreVisible(int)
	 */

	public boolean ownDeletesAreVisible(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#ownInsertsAreVisible(int)
	 */

	public boolean ownInsertsAreVisible(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#ownUpdatesAreVisible(int)
	 */

	public boolean ownUpdatesAreVisible(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#storesLowerCaseIdentifiers()
	 */

	public boolean storesLowerCaseIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#storesLowerCaseQuotedIdentifiers()
	 */

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#storesMixedCaseIdentifiers()
	 */

	public boolean storesMixedCaseIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#storesMixedCaseQuotedIdentifiers()
	 */

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#storesUpperCaseIdentifiers()
	 */

	public boolean storesUpperCaseIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#storesUpperCaseQuotedIdentifiers()
	 */

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsANSI92EntryLevelSQL()
	 */

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsANSI92FullSQL()
	 */

	public boolean supportsANSI92FullSQL() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsANSI92IntermediateSQL()
	 */

	public boolean supportsANSI92IntermediateSQL() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsAlterTableWithAddColumn()
	 */

	public boolean supportsAlterTableWithAddColumn() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsAlterTableWithDropColumn()
	 */

	public boolean supportsAlterTableWithDropColumn() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsBatchUpdates()
	 */

	public boolean supportsBatchUpdates() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInDataManipulation()
	 */

	public boolean supportsCatalogsInDataManipulation() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInIndexDefinitions()
	 */

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInPrivilegeDefinitions()
	 */

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInProcedureCalls()
	 */

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInTableDefinitions()
	 */

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsColumnAliasing()
	 */

	public boolean supportsColumnAliasing() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsConvert()
	 */

	public boolean supportsConvert() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsConvert(int, int)
	 */

	public boolean supportsConvert(int arg0, int arg1) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsCoreSQLGrammar()
	 */

	public boolean supportsCoreSQLGrammar() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsCorrelatedSubqueries()
	 */

	public boolean supportsCorrelatedSubqueries() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.sql.DatabaseMetaData#
	 * supportsDataDefinitionAndDataManipulationTransactions()
	 */

	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsDataManipulationTransactionsOnly()
	 */

	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsDifferentTableCorrelationNames()
	 */

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsExpressionsInOrderBy()
	 */

	public boolean supportsExpressionsInOrderBy() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsExtendedSQLGrammar()
	 */

	public boolean supportsExtendedSQLGrammar() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsFullOuterJoins()
	 */

	public boolean supportsFullOuterJoins() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsGetGeneratedKeys()
	 */

	public boolean supportsGetGeneratedKeys() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsGroupBy()
	 */

	public boolean supportsGroupBy() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsGroupByBeyondSelect()
	 */

	public boolean supportsGroupByBeyondSelect() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsGroupByUnrelated()
	 */

	public boolean supportsGroupByUnrelated() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsIntegrityEnhancementFacility()
	 */

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsLikeEscapeClause()
	 */

	public boolean supportsLikeEscapeClause() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsLimitedOuterJoins()
	 */

	public boolean supportsLimitedOuterJoins() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsMinimumSQLGrammar()
	 */

	public boolean supportsMinimumSQLGrammar() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsMixedCaseIdentifiers()
	 */

	public boolean supportsMixedCaseIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsMixedCaseQuotedIdentifiers()
	 */

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsMultipleOpenResults()
	 */

	public boolean supportsMultipleOpenResults() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsMultipleResultSets()
	 */

	public boolean supportsMultipleResultSets() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsMultipleTransactions()
	 */

	public boolean supportsMultipleTransactions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsNamedParameters()
	 */

	public boolean supportsNamedParameters() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsNonNullableColumns()
	 */

	public boolean supportsNonNullableColumns() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossCommit()
	 */

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossRollback()
	 */

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossCommit()
	 */

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossRollback()
	 */

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsOrderByUnrelated()
	 */

	public boolean supportsOrderByUnrelated() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsOuterJoins()
	 */

	public boolean supportsOuterJoins() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsPositionedDelete()
	 */

	public boolean supportsPositionedDelete() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsPositionedUpdate()
	 */

	public boolean supportsPositionedUpdate() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)
	 */

	public boolean supportsResultSetConcurrency(int arg0, int arg1) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsResultSetHoldability(int)
	 */

	public boolean supportsResultSetHoldability(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsResultSetType(int)
	 */

	public boolean supportsResultSetType(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSavepoints()
	 */

	public boolean supportsSavepoints() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSchemasInDataManipulation()
	 */

	public boolean supportsSchemasInDataManipulation() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSchemasInIndexDefinitions()
	 */

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSchemasInPrivilegeDefinitions()
	 */

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSchemasInProcedureCalls()
	 */

	public boolean supportsSchemasInProcedureCalls() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSchemasInTableDefinitions()
	 */

	public boolean supportsSchemasInTableDefinitions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSelectForUpdate()
	 */

	public boolean supportsSelectForUpdate() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsStatementPooling()
	 */

	public boolean supportsStatementPooling() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsStoredFunctionsUsingCallSyntax()
	 */

	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsStoredProcedures()
	 */

	public boolean supportsStoredProcedures() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInComparisons()
	 */

	public boolean supportsSubqueriesInComparisons() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInExists()
	 */

	public boolean supportsSubqueriesInExists() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInIns()
	 */

	public boolean supportsSubqueriesInIns() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInQuantifieds()
	 */

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsTableCorrelationNames()
	 */

	public boolean supportsTableCorrelationNames() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsTransactionIsolationLevel(int)
	 */

	public boolean supportsTransactionIsolationLevel(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsTransactions()
	 */

	public boolean supportsTransactions() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsUnion()
	 */

	public boolean supportsUnion() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#supportsUnionAll()
	 */

	public boolean supportsUnionAll() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#updatesAreDetected(int)
	 */

	public boolean updatesAreDetected(int arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#usesLocalFilePerTable()
	 */

	public boolean usesLocalFilePerTable() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.DatabaseMetaData#usesLocalFiles()
	 */

	public boolean usesLocalFiles() throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */

	public <T> T unwrap(Class<T> arg0) throws SQLException {

		return null;
	}

}
