package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.parser.dml.expression.ICondition;

public class PRequest {

	private String schemaName;
	
	/**
	 * Nuove clausole 
	 */
	private String clausole;
	
	/**
	 * Posizione corrente (cambia quando arriva un OR)
	 */
	private int currentPos;
	
	private ICondition condition;

	public PRequest( String clausola  ) {
		this.clausole = clausola; 
	}
	
	public PRequest() {
		this.clausole = "";
	}
	
	public void AND(String clausola) {
		this.clausole += ","+clausola;
	}

	public String getPsql() {
		return this.clausole;
	}
	
	
}
