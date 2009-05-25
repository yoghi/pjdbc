package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;

import it.unibo.lmc.pjdbc.parser.schema.TableField;

public class MinorCondition implements IComparativeCondition {

	private boolean equals = false;
	
	public MinorCondition() {}
	
	public MinorCondition(boolean equal) {
		this.equals = equal;
	}

	public boolean eval(String itemA, String itemB) {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] eval(TableField itemA, String itemB) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] eval(TableField itemA, TableField itemB) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] eval(String itemA, TableField itemB) {
		// TODO Auto-generated method stub
		return null;
	}

}
