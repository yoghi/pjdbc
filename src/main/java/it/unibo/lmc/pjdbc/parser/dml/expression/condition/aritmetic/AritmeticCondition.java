package it.unibo.lmc.pjdbc.parser.dml.expression.condition.aritmetic;

import it.unibo.lmc.pjdbc.parser.dml.expression.ICondition;
import it.unibo.lmc.pjdbc.parser.schema.TableField;


public class AritmeticCondition implements ICondition {
	
	private String op;
	
	public AritmeticCondition(String ops) {
		this.op = ops;
	}
	
	public String toString(){
		if ( op.equalsIgnoreCase("+") ) return "+";
		else if ( op.equalsIgnoreCase("-") ) return "-";
		else if ( op.equalsIgnoreCase("/") ) return "/";
		else if ( op.equalsIgnoreCase("*") ) return "*";
		return op;
	}
	
	public int eval(int A,int B){
		
		if ( op.equalsIgnoreCase("+") ) return A+B;
		else if ( op.equalsIgnoreCase("-") ) return A-B;
		else if ( op.equalsIgnoreCase("/") ) return A/B;
		else if ( op.equalsIgnoreCase("*") ) return A*B;
		return 0;
		
	}

//	public string[] eval(TableField A,int B){
//		
//	}

}
