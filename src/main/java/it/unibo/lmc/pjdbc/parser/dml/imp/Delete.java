package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.schema.Table;


public class Delete extends Select  {

	public Delete() {
		super();
	}

	@Override
	public String toString() {
		Table table = this.getFromTable().get(0);
		return "delete from "+table.getSchemaName()+"."+table.getName()+" where "+this.getWhereClausole().toString();
	}

}
