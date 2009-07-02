package it.unibo.lmc.pjdbc.parser.dml.imp;

import java.util.HashMap;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.schema.Table;

/**
 * In questo caso ho una richiesta del tipo 
 * 
 * UPDATE table_name SET (campo=valore,..) WHERE (expression);
 * 
 * @author yoghi
 *
 */
public class Update extends ParsedCommand {

	private Table table;
	
	private HashMap<String, Object> updates = new HashMap<String, Object>();
	
	public Update() {
		super();
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
	 * @param expression
	 */
	public void update(String columnName,Expression expression){
		this.updates.put(columnName, expression);
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
