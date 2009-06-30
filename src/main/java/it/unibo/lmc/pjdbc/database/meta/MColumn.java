package it.unibo.lmc.pjdbc.database.meta;

import it.unibo.lmc.pjdbc.database.utils.PTypes;

public class MColumn {

	private MSchema schema;
	private MTable table;
	private PTypes type;	//java.sql.Types
	private String name;

	/**
	 * 
	 * @param schema
	 * @param table
	 * @param columnName
	 * @param columnType
	 */
	public MColumn(MSchema schema, MTable table, String columnName, PTypes columnType) {
		this.name = columnName;
		this.type = columnType;
		this.schema = schema;
		this.table = table;
	}

	public String getColumnName() {
		return this.name;
	}

	public PTypes getColumnType() {
		return this.type;	//va bene???
	}

	public String getSchemaName() {
		return this.schema.getSchemaName();
	}

	public String getTableName() {
		return this.table.getTableName();
	}

	public String getQualifiedName() {
		return this.schema.getSchemaName()+"."+this.table.getTableName()+"."+this.name;
	} 
	
}
