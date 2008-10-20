package it.unibo.lmc.pjdbc.core.request;

import java.util.HashMap;

import it.unibo.lmc.pjdbc.core.Expression;
import it.unibo.lmc.pjdbc.core.schema.Table;

/**
 * In questo caso ho una richiesta del tipo 
 * 
 * UPDATE table_name SET (campo=valore,..) WHERE (expression);
 * 
 * @author yoghi
 *
 */
public class Update extends ParsedRequest {

	private Table table;
	
	private HashMap<String, String> updates = new HashMap<String, String>();
	
	public Update(String schema) {
		super(schema);
	}
	
	/**
	 * Lavoro solo su una tabella!!
	 */
	public void setTable(Table t){
		this.table = t;
	}
	
	/**
	 * Restituisco la tabella su cui si deve lavorare per l'update
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
	public void update(String columnName,String newValue){
		this.updates.put(columnName, newValue);
	}
	
	/**
	 * Restituisco la lista degli aggiornamenti da fare
	 * @return
	 */
	public HashMap getUpdates(){
		return this.updates;
	}
	
	//TODO: da fare la gestione della clausola WHERE
	public void setWhere(Expression where){
		
	}
}
