package it.unibo.lmc.pjdbc.core.request.imp;

import it.unibo.lmc.pjdbc.core.expression.Expression;
import it.unibo.lmc.pjdbc.core.request.ParsedRequest;
import it.unibo.lmc.pjdbc.core.schema.Table;

public class Delete extends ParsedRequest {

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
