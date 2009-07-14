/**
 * 
 */
package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.core.Catalog;
import it.unibo.lmc.pjdbc.database.meta.MColumn;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.database.utils.PTypes;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Term;


public class PrologMetaData implements DatabaseMetaData {
	
	private PrologDatabase database;
	private PrologConnection connection;
	
	/**
	 * Logger 
	 */
	private Logger log = null;

	public PrologMetaData(PrologDatabase db, PrologConnection connection) {
		this.database = db;
		this.connection = connection;
		log = Logger.getLogger(PrologMetaData.class);
	}

	public boolean allProceduresAreCallable() throws SQLException {
		return false;
	}

	public boolean allTablesAreSelectable() throws SQLException {
		return true;
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		return true;
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		return false;
	}

	public boolean deletesAreDetected(int type) throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		return false;
	}

	public ResultSet getAttributes(String catalog, String schemaPattern,String typeNamePattern, String attributeNamePattern)throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getBestRowIdentifier(String catalog, String schema,
			String table, int scope, boolean nullable) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getCatalogSeparator() throws SQLException {
		return ".";
	}

	public String getCatalogTerm() throws SQLException {
		return "catalog";
	}

	public ResultSet getCatalogs() throws SQLException {
		Catalog catalog = this.database.getCatalog();
		
		try {
			
			LinkedList<Term[]> rows = new LinkedList<Term[]>();
			LinkedList<TableField> fields = new LinkedList<TableField>();
			TableField tf = new TableField();
			tf.setAlias("TABLE_CAT");
			fields.add(tf);
			
			Term[] affectedRows = new Term[1];
			affectedRows[0] = Term.createTerm(catalog.getName());
			rows.add(affectedRows);
			
			PResultSet res = new PResultSet(fields, rows);
			
			return new PrologResultSet("", res, this.database, null);
		
		} catch (InvalidTermException e) {
			throw new PSQLException("errore nella creazione di un term",PSQLState.SYNTAX_ERROR);
		}
		
	}

	public ResultSet getColumnPrivileges(String catalog, String schema,
			String table, String columnNamePattern) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getColumns(String catalog, String schemaPattern,
			String tableNamePattern, String columnNamePattern)
			throws SQLException {
		
		MSchema mSchema = this.database.getMetaSchema(schemaPattern);
		
		if (  null == mSchema  ) throw new PSQLException("schema non valido: "+schemaPattern, PSQLState.INVALID_SCHEMA);
		
		MTable tSchema = mSchema.getMetaTableInfo(tableNamePattern);
		
		if (  null == tSchema ) throw new PSQLException("table non valida: "+tableNamePattern, PSQLState.INVALID_NAME);
		
		MColumn column = tSchema.getColumnMeta(columnNamePattern);
		
		if ( null != column  ){

			try {
				
				LinkedList<Term[]> rows = new LinkedList<Term[]>();
				LinkedList<TableField> fields = new LinkedList<TableField>();
				
				TableField tf = new TableField();
				tf.setAlias("TABLE_CAT");
				fields.add(tf);
				
				tf = new TableField();
				tf.setAlias("TABLE_SCHEM");
				fields.add(tf);
				
				tf = new TableField();
				tf.setAlias("TABLE_NAME");
				fields.add(tf);
				
				tf = new TableField();
				tf.setAlias("COLUMN_NAME");
				fields.add(tf);
				
				tf = new TableField();
				tf.setAlias("DATA_TYPE");
				fields.add(tf);
				
				tf = new TableField();
				tf.setAlias("TYPE_NAME");
				fields.add(tf);
				
				tf = new TableField();
				tf.setAlias("COLUMN_SIZE");
				fields.add(tf);
				
				
				
				Term[] affectedRows = new Term[7];
				affectedRows[0] = Term.createTerm(this.database.getCatalog().getName());
				affectedRows[1] = Term.createTerm(schemaPattern);
				affectedRows[2] = Term.createTerm(tableNamePattern);
				affectedRows[3] = Term.createTerm(column.getColumnName());
				affectedRows[4] = Term.createTerm(""+column.getColumnType().ordinal());
				affectedRows[5] = Term.createTerm(column.getColumnType().toString());
				affectedRows[6] = Term.createTerm("255");
				rows.add(affectedRows);
				
				PResultSet res = new PResultSet(fields, rows);
				
				return new PrologResultSet("", res, this.database, null);
			
			} catch (InvalidTermException e) {
				throw new PSQLException("errore nella creazione di un term",PSQLState.SYNTAX_ERROR);
			}
			
		}
			
		
		
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public Connection getConnection() throws SQLException {
		return this.connection;
	}

	public ResultSet getCrossReference(String primaryCatalog,
			String primarySchema, String primaryTable, String foreignCatalog,
			String foreignSchema, String foreignTable) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public int getDatabaseMajorVersion() throws SQLException {	
		return 0;
	}

	public int getDatabaseMinorVersion() throws SQLException {
		return 0;
	}

	public String getDatabaseProductName() throws SQLException {
		return "Prolog Database";
	}

	public String getDatabaseProductVersion() throws SQLException {
		return "0.0.1";
	}

	public int getDefaultTransactionIsolation() throws SQLException {
		return 1;	//Connection.TRANSACTION_READ_UNCOMMITTED = 1
	}

	public int getDriverMajorVersion() {
		return 0;
	}

	public int getDriverMinorVersion() {
		return 0;
	}

	public String getDriverName() throws SQLException {
		return "Stefano Tamagnini Prolog Database Driver JDBC";
	}

	public String getDriverVersion() throws SQLException {
		return "0.0.1";
	}

	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getExtraNameCharacters() throws SQLException {
		return "";
	}

	public String getIdentifierQuoteString() throws SQLException {	
		return "'";
	}

	public ResultSet getImportedKeys(String catalog, String schema, String table)throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getIndexInfo(String catalog, String schema, String table,boolean unique, boolean approximate) throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public int getJDBCMajorVersion() throws SQLException {
		return 0;
	}

	public int getJDBCMinorVersion() throws SQLException {
		return 0;
	}

	public int getMaxBinaryLiteralLength() throws SQLException {
		return 0;
	}

	public int getMaxCatalogNameLength() throws SQLException {
		return 255;
	}

	public int getMaxCharLiteralLength() throws SQLException {
		return 255;
	}

	public int getMaxColumnNameLength() throws SQLException {
		return 255;
	}

	public int getMaxColumnsInGroupBy() throws SQLException {
		return 0;
	}

	public int getMaxColumnsInIndex() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnsInOrderBy() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnsInSelect() throws SQLException {
		return 255;
	}

	public int getMaxColumnsInTable() throws SQLException {
		return 255;
	}

	public int getMaxConnections() throws SQLException {
		return 1;
	}

	public int getMaxCursorNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxIndexLength() throws SQLException {
		
		return 0;
	}

	public int getMaxProcedureNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxRowSize() throws SQLException {
		
		return 0;
	}

	public int getMaxSchemaNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxStatementLength() throws SQLException {
		
		return 0;
	}

	public int getMaxStatements() throws SQLException {
		
		return 0;
	}

	public int getMaxTableNameLength() throws SQLException {
		
		return 0;
	}

	public int getMaxTablesInSelect() throws SQLException {
		
		return 0;
	}

	public int getMaxUserNameLength() throws SQLException {
		
		return 0;
	}

	public String getNumericFunctions() throws SQLException {
		return "";
	}

	public ResultSet getPrimaryKeys(String catalog, String schema, String table)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getProcedureColumns(String catalog, String schemaPattern,
			String procedureNamePattern, String columnNamePattern)
			throws SQLException {
		
		throw new PSQLException("getProcedureColumns", PSQLState.NOT_IMPLEMENTED);
	}

	public String getProcedureTerm() throws SQLException {
		return "";
	}

	public ResultSet getProcedures(String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException {
		
		LinkedList<Term[]> rows = new LinkedList<Term[]>();
		LinkedList<TableField> fields = new LinkedList<TableField>();
		TableField tf = new TableField();
		tf.setAlias("PROCEDURE_SCAT");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("PROCEDURE_SCHEM");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("PROCEDURE_NAME");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("REMARKS");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("PROCEDURE_TYPE");
		fields.add(tf);
		
		PResultSet res = new PResultSet(fields, rows);
		
		return new PrologResultSet("", res, this.database, null);
		
	}

	public int getResultSetHoldability() throws SQLException {
		
		return 0;
	}

	/**
     * Retrieves a comma-separated list of all of this database's SQL keywords
     * that are NOT also SQL92 keywords.
     *
     * @return the list of this database's keywords that are not also
     *         SQL92 keywords
     * @exception SQLException if a database access error occurs
     */
	public String getSQLKeywords() throws SQLException {
		return "";
	}

	public int getSQLStateType() throws SQLException {
		return sqlStateSQL99;
	}

	public String getSchemaTerm() throws SQLException {
		return "schema";
	}

	public ResultSet getSchemas() throws SQLException {
		
		log.debug("getSchemas request");
		
		List<String> nameSchemas = this.database.getCatalog().getListSchemaName();
		
		try {
			
			LinkedList<Term[]> rows = new LinkedList<Term[]>();
			LinkedList<TableField> fields = new LinkedList<TableField>();
			
			TableField tf = new TableField();
			tf.setAlias("TABLE_SCHEM");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("TABLE_CAT");
			fields.add(tf);
			
			String catalogName = this.database.getCatalog().getName();
			
			Term[] affectedRows;  
			for (String name : nameSchemas) {
				log.debug("add "+catalogName+" => "+name);
				affectedRows = new Term[2];
				affectedRows[0] = Term.createTerm(name);
				affectedRows[1] = Term.createTerm(catalogName);
				rows.add(affectedRows);
			}
			
			PResultSet res = new PResultSet(fields, rows);
			
			return new PrologResultSet("", res, this.database, null);
		
		} catch (InvalidTermException e) {
			throw new PSQLException("errore nella creazione di un term",PSQLState.SYNTAX_ERROR);
		}
	}

	public String getSearchStringEscape() throws SQLException {
		return "'";
	}

	public String getStringFunctions() throws SQLException {
		return "";
	}

	public ResultSet getSuperTables(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		
		LinkedList<Term[]> rows = new LinkedList<Term[]>();
		LinkedList<TableField> fields = new LinkedList<TableField>();
		TableField tf = new TableField();
		tf.setAlias("TABLE_CAT");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TABLE_SCHEM");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TABLE_NAME");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("SUPERTABLE_NAME");
		fields.add(tf);
		
		PResultSet res = new PResultSet(fields, rows);
		
		return new PrologResultSet("", res, this.database, null);
		
	}

	public ResultSet getSuperTypes(String catalog, String schemaPattern,
			String typeNamePattern) throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getSystemFunctions() throws SQLException {
		return "";
	}

	public ResultSet getTablePrivileges(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getTableTypes() throws SQLException {
		
		try {
			
			LinkedList<Term[]> rows = new LinkedList<Term[]>();
			LinkedList<TableField> fields = new LinkedList<TableField>();
			TableField tf = new TableField();
			tf.setAlias("TABLE_TYPE");
			fields.add(tf);
			
			Term[] affectedRows = new Term[1];
			affectedRows[0] = Term.createTerm("'TABLE'");
			rows.add(affectedRows);
			
			//affectedRows = new Term[1];
			//affectedRows[0] = Term.createTerm("'SYSTEM TABLE'");
			//rows.add(affectedRows);
			
			PResultSet res = new PResultSet(fields, rows);
			
			return new PrologResultSet("", res, this.database, null);
		
		} catch (InvalidTermException e) {
			throw new PSQLException("errore nella creazione di un term "+e.getLocalizedMessage(),PSQLState.SYNTAX_ERROR);
		}
		
	}

	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
		
		if ( null == tableNamePattern ) {
			//throw new PSQLException("tablePattern cannot null", PSQLState.INVALID_NAME);
			log.error("tableNamePattern null ... considero come se fosse ANY");
		}
		
		LinkedList<Term[]> rows = new LinkedList<Term[]>();
		LinkedList<TableField> fields = new LinkedList<TableField>();
		
		TableField tf = new TableField();
		tf.setAlias("TABLE_CAT");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TABLE_SCHEM");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TABLE_NAME");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TABLE_TYPE");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("REMARKS");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TYPE_CAT");	//null
		fields.add(tf);
		tf = new TableField();
		tf.setAlias("TYPE_SCHEM");	//null
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TYPE_NAME");	//null
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("SELF_REFERENCING_COL_NAME");	//null
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("REF_GENERATION");	//null
		fields.add(tf);
		
		
		if (  null == schemaPattern  ) {
			
			List<String> schemaNames =  this.database.getCatalog().getListSchemaName();
			
			for(String nameS : schemaNames){
				
				MSchema mSchema = this.database.getMetaSchema(nameS);
				
				if ( null == mSchema ) log.error("come fa a essere null un campo che ho interno io?? nameS : "+nameS);
				
				if ( null == tableNamePattern ) {
					
					log.debug("cerco su tutti gli schema tutte le tabelle ");
					
					LinkedList<String> tablesName = mSchema.getListTableName();
					
					for(String tableName : tablesName) {
					
						MTable tSchema = mSchema.getMetaTableInfo(tableName);
						
						if ( null != tSchema ){
							
							try {
								Term[] affectedRows = new Term[10];
								affectedRows[0] = Term.createTerm(this.database.getCatalog().getName());
								affectedRows[1] = Term.createTerm(nameS);
								affectedRows[2] = Term.createTerm(tableName);
								affectedRows[3] = Term.createTerm("'TABLE'");
								affectedRows[4] = null;
								affectedRows[5] = null;
								affectedRows[6] = null;
								affectedRows[7] = null;
								affectedRows[8] = null;
								affectedRows[9] = null;
								rows.add(affectedRows);
							} catch (InvalidTermException e) {
								throw new PSQLException("errore nella creazione di un term "+e.getLocalizedMessage(),PSQLState.SYNTAX_ERROR);
							}
						}
					
					}
					
				} else {
					
					log.debug("cerco su tutti gli schema il pattern "+tableNamePattern);
					
					MTable tSchema = mSchema.getMetaTableInfo(tableNamePattern);
					
					if ( null != tSchema ){
						
						try {
							Term[] affectedRows = new Term[10];
							affectedRows[0] = Term.createTerm(this.database.getCatalog().getName());
							affectedRows[1] = Term.createTerm(nameS);
							affectedRows[2] = Term.createTerm(tableNamePattern);
							affectedRows[3] = Term.createTerm("'TABLE'");
							affectedRows[4] = null;
							affectedRows[5] = null;
							affectedRows[6] = null;
							affectedRows[7] = null;
							affectedRows[8] = null;
							affectedRows[9] = null;
							rows.add(affectedRows);
						} catch (InvalidTermException e) {
							throw new PSQLException("errore nella creazione di un term",PSQLState.SYNTAX_ERROR);
						}
					}
					
				}
				
				
			} //for
			
			
		} else {
			
			MSchema mSchema = this.database.getMetaSchema(schemaPattern);
			
			if (  null == mSchema  ) throw new PSQLException("schema non valido: "+schemaPattern, PSQLState.INVALID_SCHEMA);
			
			if ( null == tableNamePattern ) {
				LinkedList<String> tablesName = mSchema.getListTableName();
				
				for(String tableName : tablesName) {
				
					MTable tSchema = mSchema.getMetaTableInfo(tableName);
					
					if ( null != tSchema ){
						
						try {
							Term[] affectedRows = new Term[10];
							affectedRows[0] = Term.createTerm(this.database.getCatalog().getName());
							affectedRows[1] = Term.createTerm(schemaPattern);
							affectedRows[2] = Term.createTerm(tableNamePattern);
							affectedRows[3] = Term.createTerm("'TABLE'");
							affectedRows[4] = null;
							affectedRows[5] = null;
							affectedRows[6] = null;
							affectedRows[7] = null;
							affectedRows[8] = null;
							affectedRows[9] = null;
							rows.add(affectedRows);
						} catch (InvalidTermException e) {
							throw new PSQLException("errore nella creazione di un term",PSQLState.SYNTAX_ERROR);
						}
					}
				
				}
				
			} else {
				
				MTable tSchema = mSchema.getMetaTableInfo(tableNamePattern);
				
				if ( null != tSchema ){
					
					try {
						Term[] affectedRows = new Term[10];
						affectedRows[0] = Term.createTerm(this.database.getCatalog().getName());
						affectedRows[1] = Term.createTerm(schemaPattern);
						affectedRows[2] = Term.createTerm(tableNamePattern);
						affectedRows[3] = Term.createTerm("'TABLE'");
						affectedRows[4] = null;
						affectedRows[5] = null;
						affectedRows[6] = null;
						affectedRows[7] = null;
						affectedRows[8] = null;
						affectedRows[9] = null;
						rows.add(affectedRows);
					} catch (InvalidTermException e) {
						throw new PSQLException("errore nella creazione di un term",PSQLState.SYNTAX_ERROR);
					}
				}
				
			}	
			
		}
		 
		
		PResultSet res = new PResultSet(fields, rows);		
		return new PrologResultSet("", res, this.database, null);

	}

	public String getTimeDateFunctions() throws SQLException {
		return "";
	}

	public ResultSet getTypeInfo() throws SQLException {
		
		try {
			
			LinkedList<Term[]> rows = new LinkedList<Term[]>();
			LinkedList<TableField> fields = new LinkedList<TableField>();
			TableField tf = new TableField();
			tf.setAlias("TYPE_NAME");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("DATA_TYPE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("PRECISION");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("LITERAL_PREFIX");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("LITERAL_SUFFIX");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("CREATE_PARAMS");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("NULLABLE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("CASE_SENSITIVE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("SEARCHABLE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("UNSIGNED_ATTRIBUTE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("FIXED_PREC_SCALE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("AUTO_INCREMENT");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("LOCAL_TYPE_NAME");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("MINIMUM_SCALE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("MAXIMUM_SCALE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("SQL_DATA_TYPE");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("SQL_DATETIME_SUB");
			fields.add(tf);
			
			tf = new TableField();
			tf.setAlias("NUM_PREC_RADIX");
			fields.add(tf);
			
			for ( PTypes type : PTypes.values() ){
				
				Term[] affectedRows = new Term[18];
				affectedRows[0] = Term.createTerm(type.name());
				affectedRows[1] = Term.createTerm(""+type.getSqlType());
				affectedRows[2] = Term.createTerm(""+1);
				affectedRows[3] = null;
				affectedRows[4] = null;
				affectedRows[5] = null;
				affectedRows[6] = Term.createTerm("typeNullable");
				affectedRows[7] = Term.createTerm("true");
				affectedRows[8] = Term.createTerm("typePredNone");
				affectedRows[9] = Term.createTerm("false"); 
				affectedRows[10] = Term.createTerm("false");
				affectedRows[11] = Term.createTerm("false");
				affectedRows[12] = null;
				affectedRows[13] = Term.createTerm("0");
				affectedRows[14] = Term.createTerm("0");
				affectedRows[15] = Term.createTerm("0");
				affectedRows[16] = Term.createTerm("0");
				affectedRows[17] = Term.createTerm("2");
				rows.add(affectedRows);
				
			}
			
			PResultSet res = new PResultSet(fields, rows);
			
			return new PrologResultSet("", res, this.database, null);
		
		} catch (InvalidTermException e) {
			throw new PSQLException("errore nella creazione di un term "+e.getLocalizedMessage(),PSQLState.SYNTAX_ERROR);
		}
		
	}

	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
		
		LinkedList<Term[]> rows = new LinkedList<Term[]>();
		LinkedList<TableField> fields = new LinkedList<TableField>();
		
		TableField tf = new TableField();
		tf.setAlias("TYPE_CAT");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TYPE_SCHEM");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("TYPE_NAME");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("CLASS_NAME");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("DATA_TYPE");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("REMARKS");
		fields.add(tf);
		
		tf = new TableField();
		tf.setAlias("BASE_TYPE");
		fields.add(tf);
		
		PResultSet res = new PResultSet(fields, rows);
		
		return new PrologResultSet("", res, this.database, null);
		
		
	}

	public String getURL() throws SQLException {
		return null;
	}

	public String getUserName() throws SQLException {
		return "";
	}

	public ResultSet getVersionColumns(String catalog, String schema,
			String table) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean insertsAreDetected(int type) throws SQLException {
		return false;
	}

	public boolean isCatalogAtStart() throws SQLException {
		return true;
	}

	public boolean isReadOnly() throws SQLException {
		return false;
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		return false;
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		return false;
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		return false;
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		return false;
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		return false;
	}

	public boolean nullsAreSortedLow() throws SQLException {
		return false;
	}

	public boolean othersDeletesAreVisible(int type) throws SQLException {
		return false;
	}

	public boolean othersInsertsAreVisible(int type) throws SQLException {
		return true;
	}

	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		return true;
	}

	public boolean ownDeletesAreVisible(int type) throws SQLException {
		return true;
	}

	public boolean ownInsertsAreVisible(int type) throws SQLException {
		return true;
	}

	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		return true;
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		return true;
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		return false;
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		return false;
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		return false;
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		return false;
	}

	public boolean supportsBatchUpdates() throws SQLException {
		return false;
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		return false;
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		return false;
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsColumnAliasing() throws SQLException {
		return true;
	}

	public boolean supportsConvert() throws SQLException {
		return false;
	}

	public boolean supportsConvert(int fromType, int toType)
			throws SQLException {
		return false;
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		return false;
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		return false;
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions()
			throws SQLException {
		return false;
	}

	public boolean supportsDataManipulationTransactionsOnly()
			throws SQLException {
		return false;
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		return false;
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		return false;
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		return false;
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		return false;
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		return false;
	}

	public boolean supportsGroupBy() throws SQLException {
		return false;
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		return false;
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		return false;
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		return false;
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		return false;
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		return false;
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		return false;
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		return false;
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		return false;
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		return false;
	}

	public boolean supportsNamedParameters() throws SQLException {
		return false;
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		return false;
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		return false;
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		return false;
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		return false;
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		return false;
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		return false;
	}

	public boolean supportsOuterJoins() throws SQLException {
		return false;
	}

	public boolean supportsPositionedDelete() throws SQLException {
		return false;
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		return false;
	}

	public boolean supportsResultSetConcurrency(int type, int concurrency)
			throws SQLException {
		return false;
	}

	public boolean supportsResultSetHoldability(int holdability)
			throws SQLException {
		return false;
	}

	public boolean supportsResultSetType(int type) throws SQLException {
		return true; //VEDI java.sql.ResultSet...
	}

	public boolean supportsSavepoints() throws SQLException {
		return false; //dovra diventare true
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		return true;
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		return false;
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		return true;
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		return false;
	}

	public boolean supportsStatementPooling() throws SQLException {
		return false;
	}

	public boolean supportsStoredProcedures() throws SQLException {
		return false;
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		return false;
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		return false;
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		return false;
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		return false;
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		return true;
	}

	public boolean supportsTransactionIsolationLevel(int level)
			throws SQLException {
		return false;
	}

	public boolean supportsTransactions() throws SQLException {
		return false;
	}

	public boolean supportsUnion() throws SQLException {
		return false;
	}

	public boolean supportsUnionAll() throws SQLException {
		return false;
	}

	public boolean updatesAreDetected(int type) throws SQLException {
		return false;
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		return true;
	}

	public boolean usesLocalFiles() throws SQLException {
		return true;
	}


}
