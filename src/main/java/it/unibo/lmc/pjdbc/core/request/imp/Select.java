package it.unibo.lmc.pjdbc.core.request.imp;

import it.unibo.lmc.pjdbc.core.Expression;
import it.unibo.lmc.pjdbc.core.request.ParsedRequest;
import it.unibo.lmc.pjdbc.core.schema.TableField;
import it.unibo.lmc.pjdbc.parser.commons.Limit;

import java.util.List;

/**
 * In questo caso ho una richiesta del tipo 
 * 
 * SELECT column_one, column_two FROM table_name WHERE (expression) <LIMIT,ORDER BY,GROUP BY> 
 * 
 * @author yoghi
 *
 */
public class Select extends ParsedRequest {

	public Select(String schema) {
		super(schema);
	}

	public void addField(TableField field) {
		// TODO Auto-generated method stub
		
	}

	public void from(List fromList) {
		// TODO Auto-generated method stub
		
	}

	public void where(Expression where) {
		// TODO Auto-generated method stub
		
	}

	public void groupBy(List group) {
		// TODO Auto-generated method stub
		
	}

	public void orderBy(List order) {
		// TODO Auto-generated method stub 
		
	}

	public void limit(Limit limit) {
		// TODO Auto-generated method stub
		
	}
	
	

}
