/**
 * 
 */
package it.unibo.lmc.pjdbc.driver;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PrologMetaData implements DatabaseMetaData {
	
	private PrologDatabase database;

	public PrologMetaData(PrologDatabase db) {
		this.database = db;
	}

	public boolean allProceduresAreCallable() throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean allTablesAreSelectable() throws SQLException {
		return true;
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean deletesAreDetected(int type) throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getAttributes(String catalog, String schemaPattern,String typeNamePattern, String attributeNamePattern)throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getBestRowIdentifier(String catalog, String schema,
			String table, int scope, boolean nullable) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getCatalogSeparator() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getCatalogTerm() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getCatalogs() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getColumnPrivileges(String catalog, String schema,
			String table, String columnNamePattern) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getColumns(String catalog, String schemaPattern,
			String tableNamePattern, String columnNamePattern)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public Connection getConnection() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
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
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getDatabaseProductVersion() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
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
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getDriverVersion() throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getExtraNameCharacters() throws SQLException {
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getIdentifierQuoteString() throws SQLException {	
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
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
		
		return 0;
	}

	public int getMaxCharLiteralLength() throws SQLException {
		
		return 0;
	}

	public int getMaxColumnNameLength() throws SQLException {
		
		return 0;
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
		
		return 0;
	}

	public int getMaxColumnsInTable() throws SQLException {
		
		return 0;
	}

	public int getMaxConnections() throws SQLException {
		
		return 0;
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
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getPrimaryKeys(String catalog, String schema, String table)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getProcedureColumns(String catalog, String schemaPattern,
			String procedureNamePattern, String columnNamePattern)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getProcedureTerm() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getProcedures(String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public int getResultSetHoldability() throws SQLException {
		
		return 0;
	}

	public String getSQLKeywords() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public int getSQLStateType() throws SQLException {
		
		return 0;
	}

	public String getSchemaTerm() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getSchemas() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getSearchStringEscape() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getStringFunctions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getSuperTables(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getSuperTypes(String catalog, String schemaPattern,
			String typeNamePattern) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getSystemFunctions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getTablePrivileges(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getTableTypes() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getTables(String catalog, String schemaPattern,
			String tableNamePattern, String[] types) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getTimeDateFunctions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getTypeInfo() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getUDTs(String catalog, String schemaPattern,
			String typeNamePattern, int[] types) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getURL() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public String getUserName() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public ResultSet getVersionColumns(String catalog, String schema,
			String table) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean insertsAreDetected(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean isCatalogAtStart() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean isReadOnly() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean nullsAreSortedLow() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean othersDeletesAreVisible(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean othersInsertsAreVisible(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean ownDeletesAreVisible(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean ownInsertsAreVisible(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsBatchUpdates() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsColumnAliasing() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsConvert() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsConvert(int fromType, int toType)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions()
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsDataManipulationTransactionsOnly()
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsGroupBy() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsNamedParameters() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsOuterJoins() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsPositionedDelete() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsResultSetConcurrency(int type, int concurrency)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsResultSetHoldability(int holdability)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsResultSetType(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSavepoints() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsStatementPooling() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsStoredProcedures() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsTransactionIsolationLevel(int level)
			throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsTransactions() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsUnion() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean supportsUnionAll() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean updatesAreDetected(int type) throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}

	public boolean usesLocalFiles() throws SQLException {
		
		throw new PSQLException("", PSQLState.NOT_IMPLEMENTED);
	}


}
