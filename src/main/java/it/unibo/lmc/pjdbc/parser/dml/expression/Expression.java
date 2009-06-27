package it.unibo.lmc.pjdbc.parser.dml.expression;

import it.unibo.lmc.pjdbc.core.command.PRequest;
import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.parser.Token;
import it.unibo.lmc.pjdbc.parser.dml.expression.condition.ICondition;
import it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative.IComparativeCondition;
import it.unibo.lmc.pjdbc.parser.dml.expression.condition.logic.AndCondition;
import it.unibo.lmc.pjdbc.parser.dml.expression.condition.logic.ILogicCondition;
import it.unibo.lmc.pjdbc.parser.dml.expression.condition.logic.OrCondition;
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
	
	public String toString(){
		if ( null != condition  ) return "[" + left.toString() + " " + this.condition.toString()  + " " + right.toString() + "]";
		else return "["+valore+"]";
	}

	public int numClausole() {
		return this.numClausole;
	}

	/**
	 * Modifico la richiesta per rispettare le informazioni presenti in Expression
	 * @param requestPsql la richiesta attuale
	 */
	public void eval(PRequest requestPsql) {
		
		if ( null == this.condition ) {
			
			// ... è solo un valore che ci faccio???
			//requestPsql.AND(this.valore);
			
		} else {
		
			if ( this.condition instanceof ILogicCondition ) {
				
				if ( this.condition instanceof AndCondition ){
					
					this.left.eval(requestPsql);
					this.right.eval(requestPsql);
					
				} else if ( this.condition instanceof OrCondition ){
					
					PRequest tempRequestA = requestPsql.duplicate();
					//PRequest tempRequestB = requestPsql.duplicate();
					
					this.left.eval(requestPsql);
					
					this.right.eval(tempRequestA);
					
					requestPsql.OR(tempRequestA.getPsql());
					
				}
				
			}
			
			if ( this.condition instanceof IComparativeCondition ) {
				
				System.out.println("comparative = <= >= < > ");
				
				// qui metto in AND la clausola X1<=10+D2 etc etc 
				
			}
		
		}
		
	}
	
}
