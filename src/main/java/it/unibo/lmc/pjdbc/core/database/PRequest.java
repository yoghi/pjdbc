package it.unibo.lmc.pjdbc.core.database;

import it.unibo.lmc.pjdbc.core.meta.MSchema;

import java.util.HashMap;

public class PRequest {

	/**
	 * Metadati sullo schema corrente
	 */
	private MSchema mschema;
	
	/**
	 * Nuove clausole 
	 */
	private String clausole;

	/**
	 * Var PSQL - Var SQL
	 */
	private HashMap<String, String> aliasVariable;
	
	/**
	 * Table Alias - Table Name
	 */
	private HashMap<String, String> aliasTable;
	

	public PRequest( MSchema schema, String clausola  ) {
		this.mschema = schema;
		this.clausole = clausola; 
	}
	
	public PRequest() {}

	/**
	 * Aggiungo una clausola a quelle da eseguire
	 * @param clausola
	 */
	public void AND(String clausola) {
		if ( null == this.clausole ) this.clausole = clausola;
		else this.clausole += ","+clausola;
	}

	/**
	 * Ritorno la richiesta sql in prolog
	 * @return string prolog request
	 */
	public String getPsql() {
		return this.clausole+".";
	}

	/**
	 * Lego alla richiesta informazioni su come è strutturato il database
	 * @param mschema2
	 */
	public void setSchemaInfo(MSchema mschema2) {
		this.mschema = mschema2;
	}

	public HashMap<String, String> getVarList() {
		return this.aliasVariable;
	}

	public void setVarInfo(HashMap<String, String> aliasVariable) {
		this.aliasVariable = aliasVariable;
	}
	
	/**
	 * Trasforma la var usata in sql in quella usate nel prolog
	 * @param var
	 * @return
	 */
	public String getVarAliasSqltoProlog(String var){
		if ( null != this.aliasVariable ) {
			
			if ( !var.contains(".") ){
			
				//posso aver inserito direttamente un alias
				// oppure
				//ho omesso il fatto che è un campo della prima tabella utilizzata
				String table_name = (String)this.aliasTable.values().toArray()[0];
				String var_research = table_name+"."+var;
				
				//controllo se var esiste
				for (String key : this.aliasVariable.keySet()) {
					if ( this.aliasVariable.get(key).equalsIgnoreCase(var_research) ) return key;
					if ( this.aliasVariable.get(key).equalsIgnoreCase(var) ) return key;
				}
				
			} else {
			
				String[] var_research = var.split("\\.");
				String table_name = this.aliasTable.get(var_research[0]);
				String var2 = table_name+"."+var_research[1];
				
				//controllo se var esiste
				for (String key : this.aliasVariable.keySet()) {
					if ( this.aliasVariable.get(key).equalsIgnoreCase(var2) ) return key;
					if ( this.aliasVariable.get(key).equalsIgnoreCase(var) ) return key;
				}
				
			}		
			
		}
		return null;
	}
	
	/**
	 * Trasforma la var usata in prolog in quella usata nell'sql
	 * @param var
	 * @return
	 */
	public String getVarAliasPrologToSql(String var){
		if ( null != this.aliasVariable ) return this.aliasVariable.get(var);
		return null; 
	}


	public void setTableInto(HashMap<String, String> aliasTable) {
		this.aliasTable = aliasTable;
	}
	
}
