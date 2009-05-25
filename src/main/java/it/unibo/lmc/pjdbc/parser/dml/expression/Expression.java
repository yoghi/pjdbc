package it.unibo.lmc.pjdbc.parser.dml.expression;

import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.parser.Token;
import it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative.IComparativeCondition;
import it.unibo.lmc.pjdbc.parser.dml.expression.condition.logic.ILogicCondition;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.HashMap;

public class Expression {

	Expression left;
	Expression right;
	ICondition condition;
	String valore;
	int numClausole;
	
	public Expression(String tk){
		valore = tk;
		this.numClausole = 1;
	}
	
	public Expression(TableField tf){
		valore = "T"+tf.toString();
		this.numClausole = 1;	
	}
	
	public Expression(Token tk){
		valore = tk.image;
		this.numClausole = 1;
	}
	
	public Expression(Expression left,Expression right,ICondition cond){
		this.condition = cond;
		this.left = left;
		this.right = right;
		this.numClausole = left.numClausole + right.numClausole;
	}
	
	/**
	 * Eseguo l'espressione
	 * @param tables le tabelle con i campi richiesti nell'espressione
	 * @param mschema metadati
	 * @param aliasVariables associazioni variabili PSQL => SQL 
	 * @return clausole aggiuntive da mettere nella interrogazione prolog
	 */
	public String[] eval(HashMap<String, TableField[]> tables, MSchema mschema, HashMap<String, String> aliasVariables){

		
		if ( this.condition instanceof ILogicCondition ) {
			
			System.out.println("logic");
			
		}
		
		if ( this.condition instanceof IComparativeCondition ) {
			
			System.out.println("comparative = <= >= < > ");
			
			if ( this.right.numClausole > 1 ){	
				
				String[] right_clausole = this.right.eval(tables, mschema, aliasVariables);
				
			}
			
			//TODO: e mo?? qui che comparazione faccio? quali sono i casi che possono capitare??
			
		}
		
		

		return null;
	}
	
	public String toString(){
		if ( null != condition  ) return "[" + left.toString() + " " + this.condition.toString()  + " " + right.toString() + "]";
		else return "["+valore+"]";
	}

	public int numClausole() {
		return this.numClausole;
	}
	
}
