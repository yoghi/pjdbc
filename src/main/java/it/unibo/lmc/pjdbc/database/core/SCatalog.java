package it.unibo.lmc.pjdbc.database.core;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.transaction.TSchema;
import it.unibo.lmc.pjdbc.database.transaction.TSchemaRU;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;

import java.io.File;
import java.util.Hashtable;

public class SCatalog extends Catalog {

	private Hashtable<String,MSchema> availableMSchema;


	public SCatalog(PrologDatabase db) {
		super(db);
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
		
		if ( nameSchema.equalsIgnoreCase("metabase")  ){
			
			MSchema schemaMetabase = new MSchema("metabase");
			
			MTable tableDatabase = new MTable(schemaMetabase, "database", 2);
			tableDatabase.setField(0, "schemaName", "string");
			tableDatabase.setField(1, "fileName", "string");
			schemaMetabase.addMetaTableInfo(tableDatabase);
			
			MTable tableTable = new MTable(schemaMetabase, "table", 5);
			tableTable.setField(0, "schemaName", "string");
			tableTable.setField(1, "tableName", "string");
			tableTable.setField(2, "columnPosition", "int");
			tableTable.setField(3, "columnName", "string");
			tableTable.setField(4, "type", "string");
			schemaMetabase.addMetaTableInfo(tableTable);
			
			//this.schemaList.put("metabase", schemaMetabase);
			this.availableMSchema.put("metabase", schemaMetabase);
			
			//analizzo il database
			PResultSet result = this.database.executeSelect("select * from metabase.mtable;");
			
			result.getFields();
			
			System.exit(1);
			
			//devo cachare il risultato...
			
		}
		
		
		
		// DEVO VALIDARE lo schema e in caso caricarlo in memoria
		//catalogSchema.validate(filePrologDB.getName(),this.current_theory);
		/*
		if ( catalogSchema.existMetaSchema(filePrologDB.getName()) ){
			this.metaSchema = catalogSchema.getMetaSchemaFromFilename(filePrologDB.getName());
		} else {
			//qui non ci posso essere se validate ha funzionato....
			log.error("sono dove non dovrei essere...");
		}
		*/
		
		
	}
	

	public MSchema validate(PSchema pSchema){
		
		return null;
	}

	public MSchema getAvailableMSchema(String nameSchema) {
		return this.availableMSchema.get(nameSchema);
	}
	
	
}
