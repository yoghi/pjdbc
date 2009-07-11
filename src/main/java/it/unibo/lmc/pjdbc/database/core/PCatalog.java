package it.unibo.lmc.pjdbc.database.core;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.transaction.TSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;

public class PCatalog extends Catalog {

	/**
	 * Nome del catalog (dirName)
	 */
	private String catalogName;
	
	/**
	 * Current 
	 */
	private String currentSchemaName;
	
	
	/**
	 * Costruttore 
	 * @param name nome del catalog (dir name)
	 * @throws PSQLException
	 */
	public PCatalog(String name,PrologDatabase db) throws PSQLException {
		super(db);
		this.catalogName = name;
	}
	
	/**
	 * Aggiungo uno schema nel sistema
	 * @param dirpath
	 * @param filename
	 * @throws PSQLException
	 */
	public void addSchema(TSchema tschema,String nameSchema) throws PSQLException{
		this.availableSchema.put(nameSchema,tschema);
		this.log.info("Caricato schema : "+nameSchema);	
		this.currentSchemaName = nameSchema;
	}

	

	public String getCurrentSchemaName() {
		return currentSchemaName;
	}

	public void setCurrentSchemaName(String schema) throws PSQLException {
		if ( this.contains(schema) ) this.currentSchemaName = schema;
		else throw new PSQLException("schema name non valido: "+schema, PSQLState.INVALID_SCHEMA);
	}

	public String getName() {
		return catalogName;
	}

}
