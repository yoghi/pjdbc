package it.unibo.lmc.pjdbc.parser.dml.imp;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.ArrayList;
import java.util.List;

/**
 * In questo caso ho una richiesta del tipo 
 * 
 * INSERT INTO table_name (campi) VALUES (valori dei campi)
 * 
 * @author yoghi
 *
 */
public class Insert extends ParsedCommand {
	
	private Table table;
	
	/**
	 * Campi da aggiornare
	 */
	private List<TableField> fields = new ArrayList<TableField>();
	
	/**
	 * Valori da inserire 
	 * Expressioni (??)
	 */
	private List<String> values = new ArrayList<String>();
	

	public Insert(String schema) {
		super(schema);
	}

	/**
	 * Lavoro solo su una tabella!!
	 */
	public void setTable(Table t){
		this.table = t;
		if ( t.getSchemaName() != this.defaultSchema ) this.defaultSchema = t.getSchemaName();
	}
	
	/**
	 * Restituisco la tabella su cui si deve lavorare per l'insert
	 * @return Table
	 */
	public Table getTable(){
		return this.table;
	}

	/**
	 * Setto i nomi delle colonne da aggiornare
	 * @param columnName
	 */
	public void setField(TableField columnName){
		this.fields.add(columnName);
	}
	
	/**
	 * Setto i valori da aggiornare
	 * @param value
	 */
	public void setValue(String value){
		this.values.add(value);
	}

	/**
	 * restituisco i campi da usare
	 * @return
	 */
	public List<TableField> getFields(){
		return this.fields;
	}
	
	/**
	 * restituisco i valori da inserire
	 * @return
	 */
	public List<String> getValues(){
		return this.values;
	}
	
	@Override
	public String toString() {
		String res = "insert into "+this.table+" ";
		
		if ( this.fields.size() > 0 ){
			res += "(";
			for (TableField field : this.fields) {
				res += field + ",";
			}
			res += ") ";
		}
		
		res += "VALUES (";
		
		for (String valore : this.values) {
			res += valore+",";
		}	
		return res+")";
	}
	
}
