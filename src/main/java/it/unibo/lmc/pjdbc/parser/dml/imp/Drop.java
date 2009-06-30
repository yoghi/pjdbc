package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.schema.Table;

import java.util.ArrayList;

/**
 * In questo caso ho una richiesta del tipo 
 * 
 * DROP table_name1, table_name2,....
 * 
 * @author yoghi
 *
 */
public class Drop extends ParsedCommand {

	private ArrayList<Table> tables = new ArrayList<Table>();
	
	public Drop(String schema) {
		super(schema);
	}
	
	public void addTable(Table t){
		this.tables.add(t);
	}
	
	public ArrayList<Table> getTablesList(){
		return this.tables;
	}

	@Override
	public String toString() {
		StringBuilder build =  new StringBuilder();
		build.append("drop table: ");
		for (Table table : this.tables) {
			build.append(table.getSchemaName()+"."+table.getName());
			build.append(",");
		}
		build.reverse();
		build.replace(0, 1, ";");
		return build.reverse().toString();
	}

}
