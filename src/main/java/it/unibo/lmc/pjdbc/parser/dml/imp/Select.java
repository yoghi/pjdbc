package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.options.Limit;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

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
public class Select extends ParsedCommand {
	
	protected List<Table> fromTable = null;
	
	protected List<TableField> campiRicerca = new ArrayList<TableField>();
	
	protected Expression whereClausole = null;

	protected Limit limit = null; 

	public Select() {
		super();
	}

	public void addField(TableField field) {
		this.campiRicerca.add(field);
	}

	public void from(List<Table> fromList) {
		this.fromTable = fromList;
	}

	public void where(Expression where) {
		this.whereClausole = where;
	}

	public void groupBy(List group) {
		// TODO da fare groupBy
	}

	public void orderBy(List order) {
		//TODO da fare OrderBy
	}

	public void limit(Limit limit) {
		this.limit = limit;
		
	}

	public String toString() {
		
		String fieldList = "";
		for (TableField t : this.campiRicerca) {
			fieldList += t+",";
		}
		
		String tableList = "";
		for (Table t : this.fromTable) {
			tableList += t+",";
		}
		
		String res = "select " + fieldList.substring(0, fieldList.length()-1) + " from "+tableList.substring(0, tableList.length()-1);
		
		if ( null != this.whereClausole ) res = res + " where " + this.whereClausole.toString() ;
		
		if ( null != this.limit ) res = res + " limit " + this.limit.toString();
		
		return res; 
	}

	public List<Table> getFromTable() {
		return fromTable;
	}

	public List<TableField> getCampiRicerca() {
		return campiRicerca;
	}

	public Expression getWhereClausole() {
		return whereClausole;
	}

	public Limit getLimit() {
		return limit;
	}


}
