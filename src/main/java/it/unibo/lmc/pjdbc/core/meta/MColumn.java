package it.unibo.lmc.pjdbc.core.meta;

public class MColumn {

	private String schema;
	private String table;
	private int type;	//java.sql.Types
	private String name;
	
	
	
	
	
	public String getColumnName() {
		return this.name;
	}





	public int getColumnType() {
		return this.type;
	}





	public String getSchemaName() {
		return this.schema;
	}





	public String getTableName() {
		return this.table;
	}





	public String getFullName() {
		return this.schema+"."+this.table+"."+this.name;
	} 
	
	
}
