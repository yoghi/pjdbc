package it.unibo.lmc.pjdbc.core.expression.condition;

import it.unibo.lmc.pjdbc.core.expression.ICondition;

public class MinorCondition implements ICondition {

	private boolean equals = false;
	
	public MinorCondition() {}
	
	public MinorCondition(boolean equal) {
		this.equals = equal;
	}
	
	public String eval(String t1, String t2) {
		// TODO Auto-generated method stub
		return null;
	}

}
