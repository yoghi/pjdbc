package it.unibo.lmc.pjdbc.core.expression;

import it.unibo.lmc.pjdbc.parser.Token;
import java.lang.reflect.Field;

public class Expression {

	Expression left;
	Expression right;
	ICondition condition;
	String valore;
	
	
	public Expression(Token tk){
		valore = tk.image;
	}
	
	public Expression(Expression left,Expression right,ICondition cond){
		this.condition = cond;
		this.left = left;
		this.right = right;
	}
	
	/**
	 * Eseguo l'espressione
	 * @return l'oggetto risultate (String o prolog number )
	 */
	public Object eval(Field... f){
		return null;
	}
	
	public String toString(){
		if ( null != condition  ) return "[" + left.toString() + " " + this.condition.toString()  + " " + right.toString() + "]";
		else return "["+valore+"]";
	}
	
}
