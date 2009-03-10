package it.unibo.lmc.pjdbc.parser.commons;

import it.unibo.lmc.pjdbc.core.schema.TableSpecificField;

public class OrderBy {
	
	private TableSpecificField column;

	/*ASCENDENTE*/
	private boolean asc = true;
	
	public void setColumnReference(TableSpecificField columnReference) {
		this.column = columnReference;
	}

	public void setAsc(boolean b) {
		this.asc = b;
	}

}
