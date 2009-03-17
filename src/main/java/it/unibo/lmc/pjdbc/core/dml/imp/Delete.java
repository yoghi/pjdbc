package it.unibo.lmc.pjdbc.core.dml.imp;

import it.unibo.lmc.pjdbc.core.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.core.dml.expression.Expression;
import it.unibo.lmc.pjdbc.core.schema.Table;

public class Delete extends ParsedCommand {

	private Table table;
	
	//se nn viene settato il where devo cancellare tutti gli elementi della cartella (??)
	public Delete(String schema) {
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
	
	//TODO: da fare la gestione della clausola WHERE
	public void setWhere(Expression where){
		
	}	
	

}
