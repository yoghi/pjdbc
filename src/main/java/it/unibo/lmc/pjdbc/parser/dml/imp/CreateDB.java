package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

public class CreateDB extends ParsedCommand {

	private String dbname;

	public CreateDB(String schema) {
		super(schema);
	}
	
	public void setDatabase(String databaseName){
		this.dbname = databaseName;
	}
	
	public String getDatabase(){
		return this.dbname;
	}

	@Override
	public String toString() {
		return "createDB "+this.defaultSchema+"."+this.dbname;
	}

}
