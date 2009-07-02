package it.unibo.lmc.pjdbc.database.command.dml;

import it.unibo.lmc.pjdbc.database.command.PClausola;
import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.meta.MColumn;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class Pselect extends PRequest {

	/**
	 * Tabella di default
	 */
	protected String primaryTable;
	
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
	
	/**
	 * Restituisco il nome per esteso del campo a partire dall'alias usato nella query 
	 * @param columnLabel
	 * @return
	 */
	public String alias2nameVar(String columnLabel) {
		return this.aliasVariables.get(columnLabel);
	}
	
	@Override
	public String generatePrologRequest() throws PSQLException {
		
		this.generateAlias();
		
		this.anyClausoleConvert();
		
		this.generateFromClausole();
		
		//where clausole
		String whereClausole = this.generateWhereClausole();
		
		StringBuilder sbuilder = new StringBuilder();
		for( String key : this.clausole.keySet()){
			
			sbuilder.append(this.clausole.get(key).toString());
			sbuilder.append(",");
			
		}
		
		if ( !whereClausole.equalsIgnoreCase("") ) {
			sbuilder.append(whereClausole);
			sbuilder.append(".");
		} else {
			sbuilder.reverse();
			sbuilder.replace(0, 1, ".");
			sbuilder.reverse();
		}
		
		return sbuilder.toString(); 
		
	}

	/**
	 * Analizzo i metadati della richiesta e genero la tabella degli Alias (Table and Var) se presenti
	 * @throws SQLException 
	 */
	protected void generateAlias() throws PSQLException {
		
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
		this.primaryTable = tb.get(0).getName();
		List<TableField> fr = ((Select)this.mcommand).getCampiRicerca();
		
		for (int i = 0; i < fr.size(); i++) {
			
			TableField current_variable = fr.get(i);
			
			if ( current_variable.getTableName() == null ) {	// id
				current_variable.setTableName(this.primaryTable);
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

			if ( !current_variable.getColumnName().equalsIgnoreCase("*") )  this.aliasVariables.put(current_variable.getAlias(), current_variable.getSchema()+"."+current_variable.getTableName()+"."+current_variable.getColumnName());
			
		}
		
	}
	
	/**
	 * Trasformo i termini * nei termini corrispondenti della tabella
	 */
	protected void anyClausoleConvert() {
		
		List<TableField> fr = ((Select)this.mcommand).getCampiRicerca();
		
		List<TableField> temp = new ArrayList<TableField>();
		
		for (TableField tableField : fr) {
			if ( tableField.getColumnName().equalsIgnoreCase("*") ) { //ANY clausola
				
				String tableName = tableField.getTableName();
				
				MColumn[] columns = this.mschema.getMetaTableInfo(tableName).getColumns();
				
				for (int i = 0; i < columns.length; i++) {
					
					TableField tempTableField = new TableField(columns[i].getColumnName());
					tempTableField.setSchema(tableField.getSchema());
					tempTableField.setTableName(tableName);
					
					tempTableField.setAlias(tableName+"."+columns[i].getColumnName());
					this.aliasVariables.put(tempTableField.getAlias(), columns[i].getQualifiedName());
					
					temp.add(tempTableField);
					
				}
				
			} else {
				temp.add(tableField);
			}
		}
		
		fr.clear();
		fr.addAll(temp);
	}
	
	/**
	 * Genero le clausole riguardanti il campo FROM
	 * @throws SQLException
	 */
	protected void generateFromClausole() throws PSQLException {
		
		List<TableField> cr = ((Select)this.mcommand).getCampiRicerca();
		
		for (TableField tf : cr) {
			
			log.debug("analizzo - monoschema : "+tf);
			
			if ( !this.clausole.containsKey(tf.getTableName()) ) throw new PSQLException("field "+tf.getTableName()+"."+tf.getColumnName()+" use non a valid table",PSQLState.UNDEFINED_COLUMN);  
			
			//la clausola è legata ad una sola schema.tabella
			PClausola prolog_clausola = this.clausole.get( tf.getTableName() );
			
			String columnName = tf.getColumnName();
			
			try {
				if ( columnName.startsWith("$")  ) {
					int pos = Integer.parseInt(columnName.substring(1));
					prolog_clausola.setTerm( this.generateNewVar(tf.getQualifiedName())  , pos, false);
				} else {
					prolog_clausola.setTerm( this.generateNewVar(tf.getQualifiedName()) , columnName, false);
				}
			} catch (ArrayIndexOutOfBoundsException e){
				log.error("colonna "+columnName+" "+e.getLocalizedMessage());
			}
			
		} //for
	}
	
	/**
	 * Genero le clausole riguardanti il campo WHERE
	 */
	protected String generateWhereClausole() {
		
		Expression exp = ((Select)this.mcommand).getWhereClausole();
		
		if ( exp == null ) return "";
		
		StringBuilder build = new StringBuilder();
		
		if ( null != exp.getLeft() ) {
			build.append(exp.getLeft());
		} else {
			if ( null != exp.getLeftF() ) {
				
				TableField tf = exp.getLeftF();
				String varSql = null;
				if ( tf.getTableName() == null ){
					varSql = this.alias2nameVar(tf.getColumnName());
				} else {
					varSql = this.alias2nameVar(tf.getTableName()+"."+tf.getColumnName());
				}
				
				String varPsql = null;
				for(String key : this.mapVariables.keySet() ){
					if ( this.mapVariables.get(key).equalsIgnoreCase(varSql) ){
						varPsql = key;
					}
				}
				
				build.append(varPsql);
				
			}
		}
		
		build.append(exp.getCondition().toString());
		
		if ( null != exp.getRight() ) {
			build.append(exp.getRight());
		} else {
			if ( null != exp.getRightF() ) {
				
				TableField tf = exp.getRightF();
				String varSql = null;
				if ( tf.getTableName() == null ){
					varSql = this.alias2nameVar(tf.getColumnName());
				} else {
					varSql = this.alias2nameVar(tf.getTableName()+"."+tf.getColumnName());
				}
				
				String varPsql = null;
				for(String key : this.mapVariables.keySet() ){
					if ( this.mapVariables.get(key).equalsIgnoreCase(varSql) ){
						varPsql = key;
					}
				}
				
				build.append(varPsql);
				
			}
		}
		
		return build.toString();
		
	}
}
