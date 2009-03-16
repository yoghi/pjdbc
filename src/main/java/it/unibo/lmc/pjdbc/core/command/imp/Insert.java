package it.unibo.lmc.pjdbc.core.command.imp;

import it.unibo.lmc.pjdbc.core.command.ParsedRequest;
import it.unibo.lmc.pjdbc.core.expression.Expression;
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
	
	private HashMap<String, Object> inserts = new HashMap<String, Object>();

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
	public void insert(String columnName,Expression expression){
		this.inserts.put(columnName, expression);
	}
	
	/**
	 * Restituisco la lista degli inserimenti da fare
	 * @return
	 */
	public HashMap getUpdates(){
		return this.inserts;
	}
	
}
