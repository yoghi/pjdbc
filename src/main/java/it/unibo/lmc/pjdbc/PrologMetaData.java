/**
 * 
 */
package it.unibo.lmc.pjdbc;

import it.unibo.lmc.pjdbc.core.schema.MetaField;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.UnknownVarException;

/**
 * @author Yoghi
 *
 */
public class PrologMetaData implements DatabaseMetaData {
	
	private Hashtable<String,ArrayList<MetaField>> table = new Hashtable<String,ArrayList<MetaField>>();

	/**
	 * Costruttore 
	 * 
	 * @throws SQLException , non posso creare un prologMetaData
	 */
	public PrologMetaData(Prolog prolog) throws SQLException{
		try {
			
			SolveInfo i = prolog.solve("metabase(TABLE,POSITION,NAME,TYPE).");
			if ( !i.isSuccess()) throw new SQLException("metabase not present");
			
			while(i.isSuccess()){
				
				Term table_name = i.getTerm("TABLE");
				Term field_position = i.getTerm("POSITION");
				Term field_name = i.getTerm("NAME");
				Term field_type = i.getTerm("TYPE");
				
				if ( table_name.isAtom() && field_name.isAtom() &&  (field_position instanceof Number)  && field_type.isAtom() ){ 
					
					ArrayList<MetaField> fields = null;
					
					if ( !this.table.containsKey(table_name.toString()) ) {
						fields = new ArrayList<MetaField>();
						this.table.put(table_name.toString(), fields);
						Logger.getLogger("it.unibo.lmc.pjdbc").debug("trovati metadati tabella "+table_name.toString());
					} else {	
						fields = (ArrayList<MetaField>) this.table.get(table_name.toString());
					}
					
					MetaField f = new MetaField(field_name.toString());
					f.setPositionInTable(((Number)field_position).intValue());
					
					if ( field_type.toString().equals("int") ) f.setType( java.sql.Types.INTEGER );
					else if ( field_type.toString().equals("string") ) f.setType( java.sql.Types.VARCHAR );
					
					fields.add(f);
					
				} else throw new SQLException("Malformed metabase");
				
				try {
					i = prolog.solveNext();
				} catch (NoMoreSolutionException e) {
					break;
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
	
	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#allProceduresAreCallable()
	 */
	public boolean allProceduresAreCallable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#allTablesAreSelectable()
	 */
	public boolean allTablesAreSelectable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#autoCommitFailureClosesAllResultSets()
	 */
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#dataDefinitionCausesTransactionCommit()
	 */
	
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#dataDefinitionIgnoredInTransactions()
	 */
	
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#deletesAreDetected(int)
	 */
	
	public boolean deletesAreDetected(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#doesMaxRowSizeIncludeBlobs()
	 */
	
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getAttributes(String arg0, String arg1, String arg2,
			String arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
	 */
	
	public ResultSet getBestRowIdentifier(String arg0, String arg1,
			String arg2, int arg3, boolean arg4) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getCatalogSeparator()
	 */
	
	public String getCatalogSeparator() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getCatalogTerm()
	 */
	
	public String getCatalogTerm() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getCatalogs()
	 */
	
	public ResultSet getCatalogs() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getClientInfoProperties()
	 */
	
	public ResultSet getClientInfoProperties() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getColumnPrivileges(String arg0, String arg1, String arg2,
			String arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 *		1.  TABLE_CAT String => table catalog (may be null)
	 *		2. TABLE_SCHEM String => table schema (may be null)
	 *		3. TABLE_NAME String => table name
	 *		4. COLUMN_NAME String => column name
	 *		5. DATA_TYPE int => SQL type from java.sql.Types
	 *		6. TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
	 *		7. COLUMN_SIZE int => column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision.
	 *		8. BUFFER_LENGTH is not used.
	 *		9. DECIMAL_DIGITS int => the number of fractional digits
	 *		10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
	 *		11. NULLABLE int => is NULL allowed.
	 *		        * columnNoNulls - might not allow NULL values
	 *		        * columnNullable - definitely allows NULL values
	 *		        * columnNullableUnknown - nullability unknown 
	 *		12. REMARKS String => comment describing column (may be null)
	 *		13. COLUMN_DEF String => default value (may be null)
	 *		14. SQL_DATA_TYPE int => unused
	 *		15. SQL_DATETIME_SUB int => unused
	 *		16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
	 *		17. ORDINAL_POSITION int => index of column in table (starting at 1)
	 *		18. IS_NULLABLE String => "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows.
	 *		19. SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
	 *		20. SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
	 *		21. SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
	 *		22. SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
	 * 
	 */
	public ResultSet getColumns(String catalog,String schemaPattern,String tableNamePattern,String columnNamePattern) throws SQLException {
		
		PrologResultSet res = new PrologResultSet();
        
        ArrayList t = (ArrayList) this.table.get(tableNamePattern);
        
        for (int i = 0; i < t.size(); i++) {
        	
        	res.moveToInsertRow();
        	MetaField f = (MetaField) t.get(i);
        	res.updateString(0, null);
        	res.updateString(1, null);
        	res.updateString(2, tableNamePattern);
        	res.updateString(3,f.getColumnName());
        	res.updateInt(4,f.getType());
        	res.updateObject(5,null);
        	res.updateObject(6,null);
        	res.updateObject(7,null);
        	res.updateObject(8,null);
        	res.updateObject(9,null);
        	res.updateObject(10,null);
        	res.updateObject(11,null);
        	res.updateObject(12,null);
        	res.updateObject(13,null);
        	res.updateObject(14,null);
        	res.updateObject(15,null);
        	res.updateInt(16,f.getPositionInTable());
        	res.insertRow();
        	
		}
         
        res.moveToCurrentRow();
        
		return res;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getConnection()
	 */
	
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getCrossReference(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDatabaseMajorVersion()
	 */
	
	public int getDatabaseMajorVersion() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDatabaseMinorVersion()
	 */
	
	public int getDatabaseMinorVersion() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDatabaseProductName()
	 */
	
	public String getDatabaseProductName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDatabaseProductVersion()
	 */
	
	public String getDatabaseProductVersion() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDefaultTransactionIsolation()
	 */
	
	public int getDefaultTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDriverMajorVersion()
	 */
	
	public int getDriverMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDriverMinorVersion()
	 */
	
	public int getDriverMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDriverName()
	 */
	
	public String getDriverName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getDriverVersion()
	 */
	
	public String getDriverVersion() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getExportedKeys(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getExtraNameCharacters()
	 */
	
	public String getExtraNameCharacters() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getFunctionColumns(String arg0, String arg1, String arg2,
			String arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getFunctions(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getIdentifierQuoteString()
	 */
	
	public String getIdentifierQuoteString() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getImportedKeys(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
	 */
	
	public ResultSet getIndexInfo(String arg0, String arg1, String arg2,
			boolean arg3, boolean arg4) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getJDBCMajorVersion()
	 */
	
	public int getJDBCMajorVersion() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getJDBCMinorVersion()
	 */
	
	public int getJDBCMinorVersion() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxBinaryLiteralLength()
	 */
	
	public int getMaxBinaryLiteralLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxCatalogNameLength()
	 */
	
	public int getMaxCatalogNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxCharLiteralLength()
	 */
	
	public int getMaxCharLiteralLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxColumnNameLength()
	 */
	
	public int getMaxColumnNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInGroupBy()
	 */
	
	public int getMaxColumnsInGroupBy() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInIndex()
	 */
	
	public int getMaxColumnsInIndex() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInOrderBy()
	 */
	
	public int getMaxColumnsInOrderBy() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInSelect()
	 */
	
	public int getMaxColumnsInSelect() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInTable()
	 */
	
	public int getMaxColumnsInTable() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxConnections()
	 */
	
	public int getMaxConnections() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxCursorNameLength()
	 */
	
	public int getMaxCursorNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxIndexLength()
	 */
	
	public int getMaxIndexLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxProcedureNameLength()
	 */
	
	public int getMaxProcedureNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxRowSize()
	 */
	
	public int getMaxRowSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxSchemaNameLength()
	 */
	
	public int getMaxSchemaNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxStatementLength()
	 */
	
	public int getMaxStatementLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxStatements()
	 */
	
	public int getMaxStatements() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxTableNameLength()
	 */
	
	public int getMaxTableNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxTablesInSelect()
	 */
	
	public int getMaxTablesInSelect() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getMaxUserNameLength()
	 */
	
	public int getMaxUserNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getNumericFunctions()
	 */
	
	public String getNumericFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getPrimaryKeys(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getProcedureColumns(String arg0, String arg1, String arg2,
			String arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getProcedureTerm()
	 */
	
	public String getProcedureTerm() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getProcedures(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getResultSetHoldability()
	 */
	
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}


	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSQLKeywords()
	 */
	
	public String getSQLKeywords() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSQLStateType()
	 */
	
	public int getSQLStateType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSchemaTerm()
	 */
	
	public String getSchemaTerm() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSchemas()
	 */
	
	public ResultSet getSchemas() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSchemas(java.lang.String, java.lang.String)
	 */
	
	public ResultSet getSchemas(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSearchStringEscape()
	 */
	
	public String getSearchStringEscape() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getStringFunctions()
	 */
	
	public String getStringFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getSuperTables(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getSuperTypes(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getSystemFunctions()
	 */
	
	public String getSystemFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getTablePrivileges(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getTableTypes()
	 */
	
	public ResultSet getTableTypes() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	
	public ResultSet getTables(String arg0, String arg1, String arg2,
			String[] arg3) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getTimeDateFunctions()
	 */
	
	public String getTimeDateFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getTypeInfo()
	 */
	
	public ResultSet getTypeInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])
	 */
	
	public ResultSet getUDTs(String arg0, String arg1, String arg2, int[] arg3)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getURL()
	 */
	
	public String getURL() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getUserName()
	 */
	
	public String getUserName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public ResultSet getVersionColumns(String arg0, String arg1, String arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#insertsAreDetected(int)
	 */
	
	public boolean insertsAreDetected(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#isCatalogAtStart()
	 */
	
	public boolean isCatalogAtStart() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#isReadOnly()
	 */
	
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#locatorsUpdateCopy()
	 */
	
	public boolean locatorsUpdateCopy() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#nullPlusNonNullIsNull()
	 */
	
	public boolean nullPlusNonNullIsNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#nullsAreSortedAtEnd()
	 */
	
	public boolean nullsAreSortedAtEnd() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#nullsAreSortedAtStart()
	 */
	
	public boolean nullsAreSortedAtStart() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#nullsAreSortedHigh()
	 */
	
	public boolean nullsAreSortedHigh() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#nullsAreSortedLow()
	 */
	
	public boolean nullsAreSortedLow() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#othersDeletesAreVisible(int)
	 */
	
	public boolean othersDeletesAreVisible(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#othersInsertsAreVisible(int)
	 */
	
	public boolean othersInsertsAreVisible(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#othersUpdatesAreVisible(int)
	 */
	
	public boolean othersUpdatesAreVisible(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#ownDeletesAreVisible(int)
	 */
	
	public boolean ownDeletesAreVisible(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#ownInsertsAreVisible(int)
	 */
	
	public boolean ownInsertsAreVisible(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#ownUpdatesAreVisible(int)
	 */
	
	public boolean ownUpdatesAreVisible(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#storesLowerCaseIdentifiers()
	 */
	
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#storesLowerCaseQuotedIdentifiers()
	 */
	
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#storesMixedCaseIdentifiers()
	 */
	
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#storesMixedCaseQuotedIdentifiers()
	 */
	
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#storesUpperCaseIdentifiers()
	 */
	
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#storesUpperCaseQuotedIdentifiers()
	 */
	
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsANSI92EntryLevelSQL()
	 */
	
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsANSI92FullSQL()
	 */
	
	public boolean supportsANSI92FullSQL() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsANSI92IntermediateSQL()
	 */
	
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsAlterTableWithAddColumn()
	 */
	
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsAlterTableWithDropColumn()
	 */
	
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsBatchUpdates()
	 */
	
	public boolean supportsBatchUpdates() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInDataManipulation()
	 */
	
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInIndexDefinitions()
	 */
	
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInPrivilegeDefinitions()
	 */
	
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInProcedureCalls()
	 */
	
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInTableDefinitions()
	 */
	
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsColumnAliasing()
	 */
	
	public boolean supportsColumnAliasing() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsConvert()
	 */
	
	public boolean supportsConvert() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsConvert(int, int)
	 */
	
	public boolean supportsConvert(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsCoreSQLGrammar()
	 */
	
	public boolean supportsCoreSQLGrammar() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsCorrelatedSubqueries()
	 */
	
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsDataDefinitionAndDataManipulationTransactions()
	 */
	
	public boolean supportsDataDefinitionAndDataManipulationTransactions()
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsDataManipulationTransactionsOnly()
	 */
	
	public boolean supportsDataManipulationTransactionsOnly()
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsDifferentTableCorrelationNames()
	 */
	
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsExpressionsInOrderBy()
	 */
	
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsExtendedSQLGrammar()
	 */
	
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsFullOuterJoins()
	 */
	
	public boolean supportsFullOuterJoins() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsGetGeneratedKeys()
	 */
	
	public boolean supportsGetGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsGroupBy()
	 */
	
	public boolean supportsGroupBy() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsGroupByBeyondSelect()
	 */
	
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsGroupByUnrelated()
	 */
	
	public boolean supportsGroupByUnrelated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsIntegrityEnhancementFacility()
	 */
	
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsLikeEscapeClause()
	 */
	
	public boolean supportsLikeEscapeClause() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsLimitedOuterJoins()
	 */
	
	public boolean supportsLimitedOuterJoins() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsMinimumSQLGrammar()
	 */
	
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsMixedCaseIdentifiers()
	 */
	
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsMixedCaseQuotedIdentifiers()
	 */
	
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsMultipleOpenResults()
	 */
	
	public boolean supportsMultipleOpenResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsMultipleResultSets()
	 */
	
	public boolean supportsMultipleResultSets() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsMultipleTransactions()
	 */
	
	public boolean supportsMultipleTransactions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsNamedParameters()
	 */
	
	public boolean supportsNamedParameters() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsNonNullableColumns()
	 */
	
	public boolean supportsNonNullableColumns() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossCommit()
	 */
	
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossRollback()
	 */
	
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossCommit()
	 */
	
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossRollback()
	 */
	
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsOrderByUnrelated()
	 */
	
	public boolean supportsOrderByUnrelated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsOuterJoins()
	 */
	
	public boolean supportsOuterJoins() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsPositionedDelete()
	 */
	
	public boolean supportsPositionedDelete() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsPositionedUpdate()
	 */
	
	public boolean supportsPositionedUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)
	 */
	
	public boolean supportsResultSetConcurrency(int arg0, int arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsResultSetHoldability(int)
	 */
	
	public boolean supportsResultSetHoldability(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsResultSetType(int)
	 */
	
	public boolean supportsResultSetType(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSavepoints()
	 */
	
	public boolean supportsSavepoints() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSchemasInDataManipulation()
	 */
	
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSchemasInIndexDefinitions()
	 */
	
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSchemasInPrivilegeDefinitions()
	 */
	
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSchemasInProcedureCalls()
	 */
	
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSchemasInTableDefinitions()
	 */
	
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSelectForUpdate()
	 */
	
	public boolean supportsSelectForUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsStatementPooling()
	 */
	
	public boolean supportsStatementPooling() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsStoredFunctionsUsingCallSyntax()
	 */
	
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsStoredProcedures()
	 */
	
	public boolean supportsStoredProcedures() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInComparisons()
	 */
	
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInExists()
	 */
	
	public boolean supportsSubqueriesInExists() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInIns()
	 */
	
	public boolean supportsSubqueriesInIns() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInQuantifieds()
	 */
	
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsTableCorrelationNames()
	 */
	
	public boolean supportsTableCorrelationNames() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsTransactionIsolationLevel(int)
	 */
	
	public boolean supportsTransactionIsolationLevel(int arg0)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsTransactions()
	 */
	
	public boolean supportsTransactions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsUnion()
	 */
	
	public boolean supportsUnion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#supportsUnionAll()
	 */
	
	public boolean supportsUnionAll() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#updatesAreDetected(int)
	 */
	
	public boolean updatesAreDetected(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#usesLocalFilePerTable()
	 */
	
	public boolean usesLocalFilePerTable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.DatabaseMetaData#usesLocalFiles()
	 */
	
	public boolean usesLocalFiles() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
