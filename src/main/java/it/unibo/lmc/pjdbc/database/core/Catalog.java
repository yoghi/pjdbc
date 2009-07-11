package it.unibo.lmc.pjdbc.database.core;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.transaction.TSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;

import java.util.Hashtable;

import org.apache.log4j.Logger;

public abstract class Catalog {

	/**
	 * Database Engine
	 */
	protected PrologDatabase database;

	/** 
	 * Schemi attivi (nome -> schema)
	 */
	protected Hashtable<String,TSchema> availableSchema = new Hashtable<String, TSchema>();
	
	/**
	 * Logger 
	 */
	protected Logger log = null;
	
	/**
	 * @param db 
	 * 
	 */
	public Catalog(PrologDatabase db) {
		log = Logger.getLogger(SCatalog.class);
		this.database = db;
	}
	
	/**
	 * Ottengo lo schema basandomi sul nome
	 * @param schema
	 * @return
	 */
	public TSchema getSchema(String schema) {
		return this.availableSchema.get(schema);
	}
	
	public MSchema getMSchema(String schemaName){
		
		//TODO da fare
		
		return null;
	}
	
	/**
	 * Verifico se uno schema è contenuto in questo catalog
	 * @param schema
	 * @return
	 */
	public boolean contains(String schema) {
		return this.availableSchema.containsKey(schema);
	}
	
	/**
	 * Chiusura degli schema
	 */
	public void close() {
		for (String schemaName : this.availableSchema.keySet()) {
			this.availableSchema.get(schemaName).close();
		}
	}
	
	abstract public void addSchema(TSchema tschema,String nameSchema) throws PSQLException;
	
}
