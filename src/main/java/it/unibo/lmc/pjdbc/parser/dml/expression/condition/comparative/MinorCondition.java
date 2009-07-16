package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;


public class MinorCondition implements IComparativeCondition {

	private boolean equals = false;
	
	public MinorCondition() {}
	
	public MinorCondition(boolean equal) {
		this.equals = equal;
	}
	
	public String toString(){
		if ( this.equals ) return "<=";
		return "<";
	}

}
