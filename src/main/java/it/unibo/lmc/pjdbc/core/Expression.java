package it.unibo.lmc.pjdbc.core;

import java.lang.reflect.Field;

import it.unibo.lmc.pjdbc.core.expression.IOperation;
import it.unibo.lmc.pjdbc.parser.Token;

public class Expression {
	
	
	public Expression(IOperation op,Token tk1, Token tk2) {
		
		//token: o numeri o variabili 
		
	}
	
	
	/**
	 * Eseguo l'espressione
	 * @return l'oggetto risultate (String o prolog number )
	 */
	public Object getValue(Field... f){
		return null;
	}
	
	
}
