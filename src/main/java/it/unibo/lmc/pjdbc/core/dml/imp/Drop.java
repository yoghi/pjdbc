package it.unibo.lmc.pjdbc.core.dml.imp;

import it.unibo.lmc.pjdbc.core.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.core.schema.Table;

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
	
	public ArrayList getTablesList(){
		return this.tables;
	}

}
