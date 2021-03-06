package it.unibo.lmc.pjdbc.parser.schema;

public class Table {

	private String schemaName;
	private String name;
	private String alias;

	public Table(String schemaName, String name) {
		this.schemaName = schemaName;
		this.name = name;
	}
	
	public Table(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setName(String string) {
		name = string;
	}

	public void setSchemaName(String string) {
		schemaName = string;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String string) {
		alias = string;
	}
	
	public String toString(){
		if ( this.schemaName != null ) {
			if ( null == this.alias ) return "["+this.schemaName+"."+this.name+"]";
			else return "["+this.schemaName+"."+this.name+" as "+this.alias+"]";
		}
		else {
			if ( null == this.alias ) {
				return "["+this.name+"]";
			} else {
				return "["+this.name+" as "+this.alias+"]";
			}
		}
	}
}
