package it.unibo.lmc.pjdbc.core.dml.expression.condition;

import it.unibo.lmc.pjdbc.core.dml.expression.ICondition;

public class AritmeticCondition implements ICondition {
	
	private String op;
	
	public AritmeticCondition(String ops) {
		this.op = ops;
	}
	
	public String eval(String t1, String t2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		if ( op.equalsIgnoreCase("+") ) return "+";
		else if ( op.equalsIgnoreCase("-") ) return "+";
		else if ( op.equalsIgnoreCase("/") ) return "/";
		else if ( op.equalsIgnoreCase("*") ) return "*";
		return op;
	}

}
