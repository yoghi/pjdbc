package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;


public class EqualCondition implements IComparativeCondition {
	
	boolean negazione = false;
	
	public EqualCondition() {}
	
	public EqualCondition(boolean negate) {
		this.negazione = negate;
	}
	
	public String toString(){
		if ( !this.negazione ) return "==";		//TODO: e il caso di comparazione tra espressioni ?? =:= 
		else return "\\==";
	}

}
