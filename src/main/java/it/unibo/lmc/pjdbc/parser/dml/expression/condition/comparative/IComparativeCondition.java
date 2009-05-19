package it.unibo.lmc.pjdbc.parser.dml.expression.condition.comparative;

import it.unibo.lmc.pjdbc.parser.dml.expression.ICondition;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

public interface IComparativeCondition extends ICondition {

	boolean eval(String itemA, String itemB);
	
	String[] eval(TableField itemA, String itemB);
	
	String[] eval(TableField itemA, TableField itemB);
	
	String[] eval(String itemA, TableField itemB);
	
}
