package it.unibo.lmc.pjdbc.parser.commons;

import it.unibo.lmc.pjdbc.core.schema.TableField;

public class OrderBy {
	
	private TableField column;

	/*ASCENDENTE*/
	private boolean asc = true;
	
	public void setColumnReference(TableField columnReference) {
		this.column = columnReference;
	}

	public void setAsc(boolean b) {
		this.asc = b;
	}

}
