package it.unibo.lmc.pjdbc.parser.dml;


public abstract class ParsedCommand {
	
	protected String defaultSchema;
	
	public ParsedCommand(String schema){
		this.defaultSchema = schema;
	}
	
	public String getSchemaName(){
		return this.defaultSchema;
	}
	
	/**
	 * Rappresentazione interna delle informazioni del comando richiesto
	 * @return la forma testuale del comando
	 */
	abstract public String toString();
	
}
