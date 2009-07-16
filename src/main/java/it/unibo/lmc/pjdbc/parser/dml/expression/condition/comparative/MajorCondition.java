package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;


public class MajorCondition implements IComparativeCondition {

	private boolean equals = false;
	
	public MajorCondition() {}
	
	public MajorCondition(boolean equal) {
		this.equals = equal;
	}
	
	public String toString(){
		if ( this.equals ) return ">=";
		return ">";
	}

}
