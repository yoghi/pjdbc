package it.unibo.lmc.pjdbc.parser.dml.expression;

import it.unibo.lmc.pjdbc.parser.dml.expression.condition.ICondition;
import it.unibo.lmc.pjdbc.parser.schema.TableField;


public class Expression {

	private ICondition condition;
	private String right;	
	private String left;
	private TableField rightF;
	private TableField leftF;
	private Expression leftExpression;
	private Expression rightExpression;
	
	public Expression(){}
	
	public void setLeftExpression(Expression leftExp){
		this.leftExpression = leftExp;
	}
	
	public void setRightExpression(Expression rightExp){
		this.rightExpression = rightExp;
	}
	
	public void setCondition(ICondition cond){
		this.condition = cond;
	}
	
	public void setLeft(String left){
		this.left = left;
	}
	
	public void setLeft(TableField left){
		this.leftF = left;
	}
	
	public void setRight(String right){
		this.right = right;
	}
	
	public void setRight(TableField right){
		this.rightF = right;
	}
	
	public String toString(){
		
		StringBuilder build = new StringBuilder();
		
		if ( null != this.left ) build.append(this.left);
		if ( null != this.leftF ) build.append(this.leftF);
		if ( null != this.leftExpression ) {
			build.append("{");
			build.append(this.leftExpression.toString());
			build.append("}");
		}
		
		//build.append(" ");
		if ( null != this.condition ) build.append(this.condition.toString());
		//build.append(" ");
		
		if ( null != this.right ) build.append(this.right);
		if ( null != this.rightF ) build.append(this.rightF);
		if ( null != this.rightExpression ) {
			build.append("{");
			build.append(this.rightExpression.toString());
			build.append("}");
		}
		
		return build.toString();
		
	}

	/**
	 * @return the condition
	 */
	public ICondition getCondition() {
		return condition;
	}

	/**
	 * @return the right
	 */
	public String getRight() {
		return right;
	}

	/**
	 * @return the left
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * @return the rightF
	 */
	public TableField getRightF() {
		return rightF;
	}

	/**
	 * @return the leftF
	 */
	public TableField getLeftF() {
		return leftF;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public Expression getRightExpression() {
		return rightExpression;
	}
	
}
