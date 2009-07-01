package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.schema.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * In questo caso ho una richiesta del tipo 
 * 
 * DROP table_name1, table_name2,....
 * 
 * @author yoghi
 *
 */
public class Drop extends ParsedCommand {

	private Map<String,Table[]> tables = new HashMap<String,Table[]>();
	
	public Drop(String schema) {
		super(schema);
	}
	
	public void addTable(Table t){
		
		if ( this.tables.containsKey(t.getSchemaName()) ){
			Table[] tables_internal = this.tables.get(t.getSchemaName());
			Table[] temp = new Table[tables_internal.length+1];
			for (int i = 0; i < tables_internal.length; i++) {
				temp[i] = tables_internal[i];
			}
			temp[tables_internal.length] = t;
			this.tables.put(t.getSchemaName(), temp);
		} else {
			Table[] temp = new Table[1];
			temp[0] = t;
			this.tables.put(t.getSchemaName(), temp);
		}
		
	}
	
	public Map<String, Table[]> getTablesList(){
		return this.tables;
	}

	@Override
	public String toString() {
		StringBuilder build =  new StringBuilder();
		build.append("drop table: ");
		for (String key : this.tables.keySet()) {
			
			Table[] tableArray = this.tables.get(key);
			for (int i = 0; i < tableArray.length; i++) {
				build.append(tableArray[i].getSchemaName()+"."+tableArray[i].getName());
				build.append(",");
			}
		}
		build.reverse();
		build.replace(0, 1, ";");
		return build.reverse().toString();
	}

}
