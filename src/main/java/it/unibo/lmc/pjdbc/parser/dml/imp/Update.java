package it.unibo.lmc.pjdbc.parser.dml.imp;

import java.util.HashMap;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

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
	
	private HashMap<TableField, String> updates = new HashMap<TableField, String>();

	private Expression whereClausole;
	
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
	public void update(TableField column,String expression){
		this.updates.put(column, expression);
	}
	
	/**
	 * Restituisco la lista degli aggiornamenti da fare
	 * @return
	 */
	public HashMap<TableField,String> getUpdates(){
		return this.updates;
	}
	
	public void setWhere(Expression where){
		this.whereClausole = where;
	}
	
	public Expression getWhereClausole() {
		return whereClausole;
	}

	@Override
	public String toString() {
		return "update "+this.table.getSchemaName()+"."+this.table.getName()+" ... where "+this.whereClausole.toString();
	}
	
}
