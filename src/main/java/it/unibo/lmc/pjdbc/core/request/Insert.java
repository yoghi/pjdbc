package it.unibo.lmc.pjdbc.core.request;

import it.unibo.lmc.pjdbc.core.schema.Table;

import java.util.HashMap;

/**
 * In questo caso ho una richiesta del tipo 
 * 
 * INSERT INTO table_name (campi) VALUES (valori dei campi)
 * 
 * @author yoghi
 *
 */
public class Insert extends ParsedRequest {
	
	private Table table;
	
	private HashMap<String, String> inserts = new HashMap<String, String>();

	public Insert(String schema) {
		super(schema);
	}

	/**
	 * Lavoro solo su una tabella!!
	 */
	public void setTable(Table t){
		this.table = t;
	}
	
	/**
	 * Restituisco la tabella su cui si deve lavorare per l'insert
	 * @return Table
	 */
	public Table getTable(){
		return this.table;
	}

	/**
	 * Setto gli aggiornamenti da fare
	 * @param columnName
	 * @param newValue
	 */
	public void insert(String columnName,String newValue){
		this.inserts.put(columnName, newValue);
	}
	
	/**
	 * Restituisco la lista degli inserimenti da fare
	 * @return
	 */
	public HashMap getUpdates(){
		return this.inserts;
	}
	
}
