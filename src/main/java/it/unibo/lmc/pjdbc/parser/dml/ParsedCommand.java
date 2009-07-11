package it.unibo.lmc.pjdbc.parser.dml;


public abstract class ParsedCommand {
	
	protected String defaultSchema;
	
	public ParsedCommand(){}
	
//	public String getSchemaName(){
//		return this.defaultSchema;
//	}
//	
//	public void setDefaultSchemaName(String schema){
//		this.defaultSchema = schema;
//	}
	
	/**
	 * Rappresentazione interna delle informazioni del comando richiesto
	 * @return la forma testuale del comando
	 */
	abstract public String toString();
	
}
