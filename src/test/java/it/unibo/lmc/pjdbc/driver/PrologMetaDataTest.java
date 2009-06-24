package it.unibo.lmc.pjdbc.driver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import junit.framework.TestCase;

public class PrologMetaDataTest extends TestCase {

	private Connection c;
	private DatabaseMetaData dbMeta;
	
	public PrologMetaDataTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	     this.c  = DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
	     this.dbMeta = c.getMetaData();
	}

	public final void testPrologMetaData() {
		fail("Not yet implemented"); // TODO
	}

	public final void testAllProceduresAreCallable() {
		fail("Not yet implemented"); // TODO
	}

	public final void testAllTablesAreSelectable() {
		fail("Not yet implemented"); // TODO
	}

	public final void testAutoCommitFailureClosesAllResultSets() {
		fail("Not yet implemented"); // TODO
	}

	public final void testDataDefinitionCausesTransactionCommit() {
		fail("Not yet implemented"); // TODO
	}

	public final void testDataDefinitionIgnoredInTransactions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testDeletesAreDetected() {
		fail("Not yet implemented"); // TODO
	}

	public final void testDoesMaxRowSizeIncludeBlobs() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetAttributes() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetBestRowIdentifier() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetCatalogSeparator() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetCatalogTerm() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetCatalogs() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetClientInfoProperties() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetColumnPrivileges() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetColumns() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetConnection() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetCrossReference() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDatabaseMajorVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDatabaseMinorVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDatabaseProductName() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDatabaseProductVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDefaultTransactionIsolation() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDriverMajorVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDriverMinorVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDriverName() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetDriverVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetExportedKeys() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetExtraNameCharacters() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetFunctionColumns() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetFunctions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetIdentifierQuoteString() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetImportedKeys() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetIndexInfo() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetJDBCMajorVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetJDBCMinorVersion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxBinaryLiteralLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxCatalogNameLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxCharLiteralLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxColumnNameLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxColumnsInGroupBy() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxColumnsInIndex() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxColumnsInOrderBy() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxColumnsInSelect() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxColumnsInTable() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxConnections() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxCursorNameLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxIndexLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxProcedureNameLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxRowSize() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxSchemaNameLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxStatementLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxStatements() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxTableNameLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxTablesInSelect() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetMaxUserNameLength() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetNumericFunctions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetPrimaryKeys() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetProcedureColumns() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetProcedureTerm() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetProcedures() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetResultSetHoldability() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSQLKeywords() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSQLStateType() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSchemaTerm() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSchemas() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSchemasStringString() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSearchStringEscape() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetStringFunctions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSuperTables() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSuperTypes() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetSystemFunctions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetTablePrivileges() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetTableTypes() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetTables() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetTimeDateFunctions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetTypeInfo() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetUDTs() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetURL() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetUserName() {
		fail("Not yet implemented"); // TODO
	}

	public final void testGetVersionColumns() {
		fail("Not yet implemented"); // TODO
	}

	public final void testInsertsAreDetected() {
		fail("Not yet implemented"); // TODO
	}

	public final void testIsCatalogAtStart() {
		fail("Not yet implemented"); // TODO
	}

	public final void testIsReadOnly() {
		fail("Not yet implemented"); // TODO
	}

	public final void testLocatorsUpdateCopy() {
		fail("Not yet implemented"); // TODO
	}

	public final void testNullPlusNonNullIsNull() {
		fail("Not yet implemented"); // TODO
	}

	public final void testNullsAreSortedAtEnd() {
		fail("Not yet implemented"); // TODO
	}

	public final void testNullsAreSortedAtStart() {
		fail("Not yet implemented"); // TODO
	}

	public final void testNullsAreSortedHigh() {
		fail("Not yet implemented"); // TODO
	}

	public final void testNullsAreSortedLow() {
		fail("Not yet implemented"); // TODO
	}

	public final void testOthersDeletesAreVisible() {
		fail("Not yet implemented"); // TODO
	}

	public final void testOthersInsertsAreVisible() {
		fail("Not yet implemented"); // TODO
	}

	public final void testOthersUpdatesAreVisible() {
		fail("Not yet implemented"); // TODO
	}

	public final void testOwnDeletesAreVisible() {
		fail("Not yet implemented"); // TODO
	}

	public final void testOwnInsertsAreVisible() {
		fail("Not yet implemented"); // TODO
	}

	public final void testOwnUpdatesAreVisible() {
		fail("Not yet implemented"); // TODO
	}

	public final void testStoresLowerCaseIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testStoresLowerCaseQuotedIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testStoresMixedCaseIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testStoresMixedCaseQuotedIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testStoresUpperCaseIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testStoresUpperCaseQuotedIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsANSI92EntryLevelSQL() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsANSI92FullSQL() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsANSI92IntermediateSQL() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsAlterTableWithAddColumn() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsAlterTableWithDropColumn() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsBatchUpdates() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsCatalogsInDataManipulation() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsCatalogsInIndexDefinitions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsCatalogsInPrivilegeDefinitions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsCatalogsInProcedureCalls() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsCatalogsInTableDefinitions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsColumnAliasing() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsConvert() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsConvertIntInt() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsCoreSQLGrammar() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsCorrelatedSubqueries() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsDataDefinitionAndDataManipulationTransactions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsDataManipulationTransactionsOnly() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsDifferentTableCorrelationNames() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsExpressionsInOrderBy() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsExtendedSQLGrammar() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsFullOuterJoins() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsGetGeneratedKeys() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsGroupBy() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsGroupByBeyondSelect() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsGroupByUnrelated() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsIntegrityEnhancementFacility() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsLikeEscapeClause() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsLimitedOuterJoins() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsMinimumSQLGrammar() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsMixedCaseIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsMixedCaseQuotedIdentifiers() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsMultipleOpenResults() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsMultipleResultSets() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsMultipleTransactions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsNamedParameters() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsNonNullableColumns() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsOpenCursorsAcrossCommit() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsOpenCursorsAcrossRollback() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsOpenStatementsAcrossCommit() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsOpenStatementsAcrossRollback() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsOrderByUnrelated() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsOuterJoins() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsPositionedDelete() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsPositionedUpdate() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsResultSetConcurrency() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsResultSetHoldability() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsResultSetType() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSavepoints() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSchemasInDataManipulation() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSchemasInIndexDefinitions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSchemasInPrivilegeDefinitions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSchemasInProcedureCalls() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSchemasInTableDefinitions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSelectForUpdate() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsStatementPooling() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsStoredFunctionsUsingCallSyntax() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsStoredProcedures() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSubqueriesInComparisons() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSubqueriesInExists() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSubqueriesInIns() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsSubqueriesInQuantifieds() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsTableCorrelationNames() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsTransactionIsolationLevel() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsTransactions() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsUnion() {
		fail("Not yet implemented"); // TODO
	}

	public final void testSupportsUnionAll() {
		fail("Not yet implemented"); // TODO
	}

	public final void testUpdatesAreDetected() {
		fail("Not yet implemented"); // TODO
	}

	public final void testUsesLocalFilePerTable() {
		fail("Not yet implemented"); // TODO
	}

	public final void testUsesLocalFiles() {
		fail("Not yet implemented"); // TODO
	}

	public final void testIsWrapperFor() {
		fail("Not yet implemented"); // TODO
	}

	public final void testUnwrap() {
		fail("Not yet implemented"); // TODO
	}

}
