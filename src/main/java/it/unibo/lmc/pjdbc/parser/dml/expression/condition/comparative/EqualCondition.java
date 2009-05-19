package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;

import it.unibo.lmc.pjdbc.parser.dml.expression.ICondition;

public class EqualCondition implements ICondition {
	
	boolean negazione = false;
	
	public EqualCondition() {}
	
	public EqualCondition(boolean negate) {
		this.negazione = negate;
	}

	public String eval(String t1, String t2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return "EQUAL";
	}

}
