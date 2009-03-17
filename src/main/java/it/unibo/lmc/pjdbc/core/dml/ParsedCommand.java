package it.unibo.lmc.pjdbc.core.dml;


public abstract class ParsedCommand implements IRequest {
	
	protected String defaultSchema;
	
	public ParsedCommand(String schema){
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
	
}
