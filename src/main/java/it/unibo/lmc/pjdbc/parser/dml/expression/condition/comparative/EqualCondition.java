package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;

import it.unibo.lmc.pjdbc.parser.schema.TableField;

public class EqualCondition implements IComparativeCondition {
	
	boolean negazione = false;
	
	public EqualCondition() {}
	
	public EqualCondition(boolean negate) {
		this.negazione = negate;
	}
	
	public String toString(){
		return "EQUAL";
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
