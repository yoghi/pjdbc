package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.core.database.PClausola;
import it.unibo.lmc.pjdbc.core.database.PRequest;
import it.unibo.lmc.pjdbc.core.database.PSQLState;
import it.unibo.lmc.pjdbc.core.meta.MColumn;
import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;
import it.unibo.lmc.pjdbc.utils.PSQLException;

import java.util.Enumeration;
import java.util.HashMap;
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
	
	/**
	 * Converto l'alias di una tabella nel suo nome per esteso
	 * @param aliasTable
	 * @return nome esteso se l'alias esiste, null altrimenti
	 */
	public String alias2nameTable(String aliasTable){
		return this.aliasTables.get(aliasTable);
	}
	
	//TODO funzione contraria data una tabella esiste il suo alias??
	
	public String alias2nameVar(String columnLabel) {
		return this.aliasVariables.get(columnLabel);
	}
	

	@Override
	public String generatePrologRequest() throws PSQLException {
		
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
	private void generateAlias() throws PSQLException {
		
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
			
			if ( null == mTableInfo ) throw new PSQLException("table : "+nome_tabella+" not found", PSQLState.UNDEFINED_TABLE);
			
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
			
			if ( current_variable.getTableName() == null ) {	// id
				current_variable.setTableName(primaryTableName);
				if ( current_variable.getAlias() == null ) current_variable.setAlias(current_variable.getColumnName());
			}
			else {	//NOTA: mi aspetto sempre 1 solo schema alla volta!!! per quello uso tablename.columname senza schema specificato!!
				if ( current_variable.getAlias() == null ) current_variable.setAlias(current_variable.getTableName()+"."+current_variable.getColumnName());
				if ( this.aliasTables.containsKey(current_variable.getTableName()) ){	// e.id
					current_variable.setTableName( this.aliasTables.get(current_variable.getTableName())  );
				} else {	//employee.id
					/** check metadati */
					MTable mTableInfo = this.mschema.getMetaTableInfo( current_variable.getTableName() );
					if ( null == mTableInfo ) throw new PSQLException(" column with undefined table : "+tb.get(i).getName()+" specified",PSQLState.UNDEFINED_COLUMN);
				}
			}

			this.aliasVariables.put(current_variable.getAlias(), current_variable.getSchema()+"."+current_variable.getTableName()+"."+current_variable.getColumnName());
			
		}
		
	}
	
	/**
	 * Genero le clausole riguardanti il campo FROM
	 * @throws SQLException
	 */
	private void generateFromClausole() throws PSQLException {
		
		List<TableField> cr = ((Select)this.mcommand).getCampiRicerca();
		
		for (TableField tf : cr) {
			
			log.debug("analizzo - monoschema : "+tf);
			
			if ( !this.clausole.containsKey(tf.getTableName()) ) throw new PSQLException("field "+tf.getTableName()+"."+tf.getColumnName()+" use non a valid table",PSQLState.UNDEFINED_COLUMN);  
			
			//la clausola è legata ad una sola schema.tabella
			PClausola prolog_clausola = this.clausole.get( tf.getTableName() );
			
			String columnName = tf.getColumnName();
			String columnTable = tf.getTableName();
			String columnSchema = tf.getSchema();
			
			try {
				if ( columnName.startsWith("$")  ) {
					int pos = Integer.parseInt(columnName.substring(1));
					prolog_clausola.setTerm( this.generateNewVar(columnSchema+"."+columnTable+"."+columnName)  , pos, false);
				} else {
					prolog_clausola.setTerm( this.generateNewVar(columnSchema+"."+columnTable+"."+columnName) , columnName, false);
				}
			} catch (ArrayIndexOutOfBoundsException e){
				log.error("colonna "+columnName+" "+e.getLocalizedMessage());
			}
			
		} //for
		
		
	}

	public List<MColumn> getFieldList() {
		
		//FieldName => Column Info
		HashMap<String, MColumn> t = new HashMap<String, MColumn>();
		
		
		
	}

//	public HashMap getMapVar() {
//
//		HashMap<String, MColumn> t = new HashMap<String, MColumn>();
//		
//		// le colonne le prendo dalla sequenza di var richieste...
//		
//		Enumeration<String> e = this.mapVariables.elements();
//		//...ciclo e ho tutte le var nell'ordine giusto...
//		//chiedo this.mschema.getMetaTableInfo() sul nome della tabella e poi .getColumInfo sulla MTable 
//		
//		
//		
//		return t;
//	}
	
}
