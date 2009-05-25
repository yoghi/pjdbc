package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;

import it.unibo.lmc.pjdbc.parser.schema.TableField;

public class MajorCondition implements IComparativeCondition {

	private boolean equals = false;
	
	public MajorCondition() {}
	
	public MajorCondition(boolean equal) {
		this.equals = equal;
	}
	
	public String toString(){
		return "MAJOR";
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
