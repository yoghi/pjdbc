package it.unibo.lmc.pjdbc.parser.dml.expression.condition.logic;

import it.unibo.lmc.pjdbc.parser.dml.expression.condition.ICondition;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

public interface ILogicCondition extends ICondition {

	String[] eval(TableField itemA, TableField itemB);
	
}
