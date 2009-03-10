package it.unibo.lmc.pjdbc.core.request.imp;

import it.unibo.lmc.pjdbc.core.Expression;
import it.unibo.lmc.pjdbc.core.request.ParsedRequest;
import it.unibo.lmc.pjdbc.core.schema.Table;
import it.unibo.lmc.pjdbc.core.schema.TableSpecificField;
import it.unibo.lmc.pjdbc.parser.commons.Limit;

import java.util.ArrayList;
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
	
	List<Table> fromTable = null;
	
	List<TableSpecificField> campiRicerca = new ArrayList<TableSpecificField>();
	
	Expression whereClausole = null;

	public Select(String schema) {
		super(schema);
	}

	public void addField(TableSpecificField field) {
		this.campiRicerca.add(field);
	}

	public void from(List<Table> fromList) {
		this.fromTable = fromList;
	}

	public void where(Expression where) {
		this.whereClausole = where;
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

	public String toString() {
		
		String fieldList = "";
		for (TableSpecificField t : this.campiRicerca) {
			fieldList += t+",";
		}
		
		String tableList = "";
		for (Table t : this.fromTable) {
			tableList += t+",";
		}
		
		String res = "select " + fieldList.substring(0, fieldList.length()-1) + " from "+tableList.substring(0, tableList.length()-1);
		
		if ( null != this.whereClausole ) res = res + " where " + this.whereClausole.toString() ;
		
		return res; 
	}
	
	

}
