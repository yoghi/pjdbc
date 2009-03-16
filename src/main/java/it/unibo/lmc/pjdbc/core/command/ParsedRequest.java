package it.unibo.lmc.pjdbc.core.command;


public abstract class ParsedRequest implements IRequest {
	
	protected String defaultSchema;
	
	public ParsedRequest(String schema){
		this.defaultSchema = schema;
	}
	
	public String getSchema(){
		return this.defaultSchema;
	}
	
	/**
	 * Rappresentazione interna delle informazioni del comando richiesto
	 * @return la forma testuale del comando
	 */
	abstract public String toString();
	
	/**
	 * Richiesta Prolog 
	 * @return la richiesta prolog che più si adatta alla richiesta SQL
	 */
//	abstract public String toProlog();
	
}
