package it.unibo.lmc.pjdbc.parser.dml.expression;

import it.unibo.lmc.pjdbc.parser.dml.expression.condition.ICondition;
import it.unibo.lmc.pjdbc.parser.schema.TableField;


public class Expression {

	private ICondition condition;
	private String right;	
	private String left;
	private TableField rightF;
	private TableField leftF;
	
	public Expression(){}
	
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
		
		build.append(" ");
		if ( null != this.condition ) build.append(this.condition.toString());
		build.append(" ");
		
		if ( null != this.right ) build.append(this.right);
		if ( null != this.rightF ) build.append(this.rightF);
		
		
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
	
}
