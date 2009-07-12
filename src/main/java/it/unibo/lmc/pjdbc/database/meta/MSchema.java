package it.unibo.lmc.pjdbc.database.meta;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class MSchema {

	/**
	 * Logger 
	 */
	private Logger log = null;
	
	/**
	 * tabelle presenti nello schema
	 */
	HashMap<String, MTable> tables = new HashMap<String, MTable>();

	/**
	 * Nome dello schema
	 */
	private String name;
	
	/**
	 * Costruttore
	 * @param schemaName nome dello schema
	 */
	public MSchema(String schemaName) {
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.meta");
		this.name = schemaName;
	}
	
	/**
	 * Ottengo le informazioni sulla tabella tname
	 * @param tname nome della tabella (senza schema)
	 * @return MTable
	 */
	public MTable getMetaTableInfo(String tname){
		return this.tables.get(tname);
	}
	
	/**
	 * Aggiugno una tabella allo schema
	 * @param table MTable
	 */
	public void addMetaTableInfo(MTable table){
		this.tables.put(table.getTableName(),table);
	}
	
	
	
	public String getSchemaName(){
		return this.name;
	}
	
	public String toString(){
		return this.name;
	}

	public LinkedList<String> getListTableName() {
		
		LinkedList<String> listName = new LinkedList<String>();
		for (String tableName : this.tables.keySet()) {
			listName.add(tableName);
		}
		return listName;
		
	}

	
//	public void printMetaInfo(PrintStream stream){
//		//stampo a video le informazioni sulla tabella
//		for (String tname : this.tables.keySet()) {
//			stream.println("==\\"+tname+"/==");
//			stream.println(this.tables.get(tname).toString());
//		}
//	}
	
}
