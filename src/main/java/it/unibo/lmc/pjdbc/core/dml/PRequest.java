package it.unibo.lmc.pjdbc.core.dml;

import java.util.HashMap;

import it.unibo.lmc.pjdbc.core.meta.MSchema;

public class PRequest {

	private MSchema mschema;
	
	/**
	 * Nuove clausole 
	 */
	private String clausole;

	/**
	 * Var PSQL - Var SQL
	 */
	private HashMap<String, String> aliasVariable;

	
	
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
	
	public String getVarAliasSqltoProlog(String var){
		if ( null != this.aliasVariable ) {
			for (String key : this.aliasVariable.keySet()) {
				if ( this.aliasVariable.get(key).equalsIgnoreCase(var) ) return key;
			}
		}
		return null;
	}
	
	public String getVarAliasPrologToSql(String var){
		if ( null != this.aliasVariable ) return this.aliasVariable.get(var);
		return null; 
	}
	
}
