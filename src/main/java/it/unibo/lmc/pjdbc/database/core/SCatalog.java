package it.unibo.lmc.pjdbc.database.core;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.transaction.TSchema;
import it.unibo.lmc.pjdbc.database.transaction.TSchemaRU;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;

import java.util.Hashtable;
import java.util.Iterator;

import com.sun.java_cup.internal.shift_action;

import alice.tuprolog.Prolog;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Number;
import alice.tuprolog.Theory;

public class SCatalog extends Catalog {

	private Hashtable<String,MSchema> availableMSchema = new Hashtable<String, MSchema>();


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
			
			MTable tableTable = new MTable(schemaMetabase, "mtable", 5);
			tableTable.setField(0, "schemaName", "string");
			tableTable.setField(1, "tableName", "string");
			tableTable.setField(2, "columnPosition", "int");
			tableTable.setField(3, "columnName", "string");
			tableTable.setField(4, "type", "string");
			schemaMetabase.addMetaTableInfo(tableTable);
			
			//this.schemaList.put("metabase", schemaMetabase);
			this.availableMSchema.put("metabase", schemaMetabase);
			
			//analizzo il database
			PResultSet result = this.database.executeQuery("select schemaName,tableName,columnPosition,columnName,type from metabase.mtable;");
			
			String nSchema, tname, cpos, cname, ctype;
			while(result.next()){
				
				nSchema = result.getValue("schemaName").toString();
				
				MSchema mSchema = this.getAvailableMSchema(nSchema);
				if ( null == mSchema ) {
					mSchema = new MSchema(nSchema);
					this.availableMSchema.put(nSchema, mSchema);
				}
				
				tname = result.getValue("tableName").toString();
				
				MTable mTable = mSchema.getMetaTableInfo(tname);
				if ( null == mTable ) {
					mTable = new MTable(mSchema,tname,1);
					mSchema.addMetaTableInfo(mTable);
				}
				
				cpos = result.getValue("columnPosition").toString();
				cname = result.getValue("columnName").toString();
				ctype = result.getValue("type").toString();
				
				if ( !mTable.containsField(cname) ) {
					mTable.setField(Integer.parseInt(cpos), cname, ctype);
				}
				
				log.debug("checked : "+mSchema.getSchemaName()+"."+mTable.getTableName());
				
			}
			
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
	

	public MSchema getAvailableMSchema(String nameSchema) {
		return this.availableMSchema.get(nameSchema);
	}
	
	public String getName(){
		return "system";
	}

	@SuppressWarnings("unchecked")
	public void validate(PSchema pSchema) throws PSQLException {
		
		Theory th = pSchema.getTheory();
		String schemaName = pSchema.getName();
		
		
		MSchema mSchema = null;
		if ( this.availableMSchema.containsKey(schemaName) ){
			mSchema = this.getAvailableMSchema(schemaName);
		}
		
		if ( null == mSchema ){
			mSchema = new MSchema(schemaName);
			this.availableMSchema.put(schemaName, mSchema);
		}
		
		Prolog engine = new Prolog();
		Iterator i = th.iterator(engine);
		
		while(i.hasNext()){
			Term t = (Term)i.next();
			if ( t instanceof Struct ){
	        	
	        	Struct s = (Struct)t;
	        	
	        	// rimane il caso "predicato(...):-!"
	        	if ( s.isGround() && s.isCompound() && !s.isList() ){
	        		//NB: X e _ sono due variabili
	        		int l = s.getArity();
	        		
	        		MTable mTable = mSchema.getMetaTableInfo(s.getName());
        			if ( null == mTable ){ //non presente nel metabase
        				mTable = new MTable(mSchema, s.getName(), l);
	        			log.debug("trovata nuova tabella "+s.getName()+" di dimensione "+l+" in "+mSchema.getSchemaName());
	        			mSchema.addMetaTableInfo(mTable);
	        			
	        			//analisi del campo
	        			for (int j = 0; j < s.getArity(); j++) {
							Term c1 = s.getArg(j);
							
							if ( c1.isList() ) mTable.setField(j, ""+j, "array");
							else if ( c1 instanceof Number ) mTable.setField(j, ""+j, "real");
							else mTable.setField(j, ""+j, "real");
							
						}
	        			
        			}
        			if ( mTable.numColum() != l ) {
        				log.warn("incoerenza tra metabase e dati sulla tabella :"+mSchema.getSchemaName()+"."+mTable.getTableName());
        				log.warn("metabase column : "+mTable.numColum()+" real column "+l);
        				//TODO decidere sul dafarsi
        			}
	        		
	        		
	        	}
	        }
		}

		
	}

	public void removeSchema(String schema) {
		
		try {
			this.database.executeQuery("delete from metabase.mtable where tableName='"+schema+"';");
		} catch (PSQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
