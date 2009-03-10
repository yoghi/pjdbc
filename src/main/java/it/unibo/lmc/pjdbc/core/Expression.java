package it.unibo.lmc.pjdbc.core;

import java.lang.reflect.Field;

import it.unibo.lmc.pjdbc.core.expression.IOperation;
import it.unibo.lmc.pjdbc.parser.Token;

public class Expression {
	
	Token left = null;
	Token right = null;
	IOperation operation = null;
	
	public Expression(IOperation op,Token tk1, Token tk2) {
		//token: o numeri o variabili 
		this.left = tk1;
		this.right = tk2;
		this.operation = op;
	}
	
	
	/**
	 * Eseguo l'espressione
	 * @return l'oggetto risultate (String o prolog number )
	 */
	public Object getValue(Field... f){
		return null;
	}
	
	public String toString(){
		return "[" + left.toString() + " " + this.operation.toString()  + " " + right.toString() + "]";
	}
	
}
