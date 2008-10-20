package it.unibo.lmc.pjdbc.core.request;


public abstract class ParsedRequest {
	
	protected String defaultSchema;
	
	public ParsedRequest(String schema){
		this.defaultSchema = schema;
	}
	
	public String getSchema(){
		return this.defaultSchema;
	}
	
}
