package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.schema.Table;

public class Delete extends ParsedCommand  {

	private Table table;
	private Expression whereClausole;

	public Delete(String schema) {
		super(schema);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWhere(Expression where) {
		this.whereClausole = where;
	}

	public void setTable(Table t) {
		this.table = t;
	}

	public Table getTable() {
		return this.table;
	}
	
	public Expression getWhereClausole(){
		return this.whereClausole;
	}

}
