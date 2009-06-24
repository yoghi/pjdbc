package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.core.database.PClausola;
import it.unibo.lmc.pjdbc.core.database.PRequest;
import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;


public class Pselect extends PRequest {

	/**
	 * Tabella di default
	 */
	private String primaryTable;
	
	/**
	 * Table: (k)Alias Table Name Sql to Extended Table Name Sql 
	 */
	protected Hashtable<String,String> aliasTables = new Hashtable<String, String>();
	
	/**
	 * Var: (k)Alias Var Name to Extended Var Name Sql 
	 */
	protected Hashtable<String,String> aliasVariables = new Hashtable<String, String>();
	
	
	/**
	 * Prolog Request di tipo Select (MONO-SCHEMA)
	 * @param ms metadati schema
	 * @param req metadati richiesta
	 */
	public Pselect(MSchema ms, Select req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws SQLException {
		
		this.generateAlias();
		
		this.generateFromClausole();
		
		//where clausole
		//this.generateWhereClausole();
		
		StringBuilder sbuilder = new StringBuilder();
		for( String key : this.clausole.keySet()){
			
			sbuilder.append(this.clausole.get(key).toString());
			sbuilder.append(",");
			
		}
		
		String pclausole = sbuilder.toString();
		
		return pclausole.subSequence(0,pclausole.length()-1)+".";
		
	}

	/**
	 * Analizzo i metadati della richiesta e genero la tabella degli Alias (Table and Var) se presenti
	 * @throws SQLException 
	 */
	private void generateAlias() throws SQLException {
		
		/**  
		 * 0. sql alias table
		 *
		 * 	FROM table AS table_alias, ...
		 * 
		 */
		List<Table> tb = ((Select)this.mcommand).getFromTable();
		
		for (int i = 0; i < tb.size(); i++) {
			
			String nome_tabella = tb.get(i).getName();
			
			/** check metadati */
			MTable mTableInfo = this.mschema.getMetaTableInfo( nome_tabella );
			
			if ( null == mTableInfo ) throw new SQLException(" invalid table : "+nome_tabella+" found");
			
			if ( !this.clausole.containsKey(nome_tabella) ) {
				this.clausole.put(nome_tabella, new PClausola(mTableInfo));
			}
			
			if ( tb.get(i).getAlias() != null ) this.aliasTables.put(tb.get(i).getAlias(), nome_tabella);
			
		}
		
		/** 
		 * 1. sql alias var || Riscrivo la struttura in modo che tutte le variabili siano estese e non usino alias!
		 * 
		 *  SELECT id AS identificativo
		 */
		String primaryTableName = tb.get(0).getName();
		List<TableField> fr = ((Select)this.mcommand).getCampiRicerca();
		
		for (int i = 0; i < fr.size(); i++) {
			
			TableField current_variable = fr.get(i);
			
			if ( current_variable.getAlias() != null ) this.aliasVariables.put(current_variable.getAlias(), current_variable.getColumnName());
			
			if ( current_variable.getTableName() == null ) current_variable.setTableName(primaryTableName);
			else {
				if ( this.aliasTables.containsKey(current_variable.getTableName()) ){
					current_variable.setTableName( this.aliasTables.get(current_variable.getTableName())  );
				} else {
					/** check metadati */
					MTable mTableInfo = this.mschema.getMetaTableInfo( current_variable.getTableName() );
					
					if ( null == mTableInfo ) throw new SQLException(" column with invalid table : "+tb.get(i).getName()+" specified");
				}
			}
		}
		
	}
	
	/**
	 * Genero le clausole riguardanti il campo FROM
	 * @throws SQLException
	 */
	private void generateFromClausole() throws SQLException {
		
		List<TableField> cr = ((Select)this.mcommand).getCampiRicerca();
		
		for (TableField tf : cr) {
			
			if ( !this.clausole.containsKey(tf.getTableName()) ) throw new SQLException("field "+tf.getTableName()+"."+tf.getColumnName()+" use non a valid table");  
			
			PClausola prolog_clausola = this.clausole.get( tf.getTableName() );
			
			String columnName = tf.getColumnName();
			
			try {
				if ( columnName.startsWith("$")  ) {
					int pos = Integer.parseInt(columnName.substring(1));
					prolog_clausola.setTerm( this.generateNewVar(columnName)  , pos, false);
				} else {
					prolog_clausola.setTerm( this.generateNewVar(columnName) , columnName, false);
				}
			} catch (ArrayIndexOutOfBoundsException e){
				log.error("colonna "+columnName+" "+e.getLocalizedMessage());
			}
			
		} //for
		
		
	}
	
}
