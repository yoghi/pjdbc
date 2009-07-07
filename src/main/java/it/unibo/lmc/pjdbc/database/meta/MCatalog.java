package it.unibo.lmc.pjdbc.database.meta;

import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.database.utils.PTypes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.media.jai.opimage.MagnitudeSquaredCRIF;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;

public class MCatalog {

	/**
	 * Metabase
	 */
	private Theory metabaseTheory;

	/**
	 * Filepath of metabase
	 */
	private String metabaseFile;
	
	/**
	 * Elenco schema disponibili
	 * Nome / Schema
	 */
	private Map<String,MSchema> schemaList = new HashMap<String, MSchema>();
	
	/**
	 * Associazione file => schema
	 */
	private Map<String,String> schemaFiles = new HashMap<String, String>();

	/**
	 * Logger 
	 */
	private Logger log = null;
	
	/**
	 * Costruttore
	 * @param metabaseFile file contenente le informazioni sul catalog
	 * @throws PSQLException
	 */
	public MCatalog( String metabaseFile ) throws PSQLException {
		this.metabaseFile = metabaseFile;
		log = Logger.getLogger(MCatalog.class);
		this.load_theory();
		this.analize_theory();
		this.selfAdd();
	}
	
	/**
	 * Aggiungo anche la descrizione del metabase => in questo modo anche se il metabase non esiste posso crearlo (??)
	 * @throws PSQLException
	 */
	private void selfAdd() throws PSQLException {
		
		log.debug("selfAdd : metabase");
		
		File f = new File(this.metabaseFile);
		this.schemaFiles.put(f.getName(), "metabase");
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
		
		this.schemaList.put("metabase", schemaMetabase);
	}

	/**
	 * Carico la teoria che descrive il catalog
	 * @throws PSQLException
	 */
	protected void load_theory() throws PSQLException {
		try {
			this.metabaseTheory = new Theory(new FileInputStream(this.metabaseFile));
			return;
		} catch (FileNotFoundException e) {
			log.error("Invalid Theory, inizialized current theory to empty!");
		} catch (IOException e) {
			log.error("Invalid Theory, inizialized current theory to empty!");
		}
		
		try {
			this.metabaseTheory = new Theory("");
		} catch (InvalidTheoryException e1) {
			log.error("Cannot inizialize empty theory");
			throw new PSQLException("Metabase malformato", PSQLState.INVALID_THEORY);
		}
	}
	
	/**
	 * Carico la teoria prolog
	 * @throws IOException 
	 */
	protected void analize_theory() throws PSQLException  {
			
		try {

			Prolog prolog = new Prolog();
			prolog.setTheory(this.metabaseTheory);

			ArrayList<SolveInfo> soluzioni = new ArrayList<SolveInfo>();
			
			SolveInfo info = prolog.solve("table(SCHEMA,TABLE,POSITION,NAME,TYPE).");
			
			if (info.isSuccess()) {

				while (info.isSuccess()) {

					soluzioni.add(info);
					
					if ( prolog.hasOpenAlternatives() ){
						try {
							info = prolog.solveNext();
						} catch (NoMoreSolutionException e) {
							break;
						}
					} else {
						break;
					}
					
				}
				
				// ora controllo le soluzioni
				for (SolveInfo solveInfo : soluzioni) {
					
					Term schema_name = solveInfo.getTerm("SCHEMA");
					
					if ( schema_name.isAtom() ){
						
						MSchema schema = null;
						if ( this.schemaList.containsKey(schema_name.toString()) ) {
							schema = this.schemaList.get(schema_name.toString());
						} else {
							schema = new MSchema(schema_name.toString());
							log.debug("aggiunto schema : "+schema_name.toString());
							this.schemaList.put(schema_name.toString(), schema);
						}
						
						Term table_name = solveInfo.getTerm("TABLE");
						Term field_position = solveInfo.getTerm("POSITION");
						Term field_name = solveInfo.getTerm("NAME");
						Term field_type = solveInfo.getTerm("TYPE");

						if (table_name.isAtom() && field_name.isAtom() && (field_position instanceof Number) && field_type.isAtom()) {

							MTable table = schema.getMetaTableInfo(table_name.toString());
							if( null == table ){
								log.debug("aggiunta tabella : "+table_name.toString());
								table = new MTable(schema, table_name.toString(), 1);
								schema.addMetaTableInfo(table);
							}

							/*
							 * se c'è qualcosa diverso da (!|[a-z][a-zA-Z_0-9]*) allora non è atomico e compare tra '
							 */
							table.setField( ((Number) field_position).intValue() , field_name.toString().replace("'", ""), field_type.toString() ) ;

						} else {
							log.error("metabase 'table' malformattato!! ");
						}	
					}
				} //for-solveinfo

			} //if-success
			
			info = prolog.solve("database(SCHEMA,FILE).");
			
			if ( info.isSuccess() ){
				
				while (info.isSuccess()) {
					
					
					Term file = info.getTerm("FILE");
					Term schema = info.getTerm("SCHEMA");

					if (file.isAtom() && schema.isAtom() ){
						this.schemaFiles.put(file.toString().replace("'", ""), schema.toString().replace("'", ""));
					} else {
						log.error("metabase 'database' malformattato!! ");
					}
					
					if ( prolog.hasOpenAlternatives() ){
						try {
							info = prolog.solveNext();
						} catch (NoMoreSolutionException e) {
							break;
						}
					} else {
						break;
					}
					
				}
				
			}
			

		} catch (MalformedGoalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSolutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownVarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTheoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Verifico se uno schema è stato registrato
	 * @param schemaFile
	 * @return
	 */
	public boolean existMetaSchema(String schemaFile){
		return this.schemaFiles.containsKey(schemaFile);
	}

	/**
	 * Restituisco lo MSchema corrispondente 
	 * @param schemaFile nome del file che contiene il database
	 * @return
	 * @throws PSQLException 
	 */
	public MSchema getMetaSchemaFromFilename(String schemaFile) throws PSQLException {
		
		String name = this.schemaFiles.get(schemaFile);
		
		if ( null == name ) throw new PSQLException("Schema filename : "+schemaFile+" sconosciuto!", PSQLState.INVALID_NAME);
		
		return this.schemaList.get(name); 
	}
	
	/**
	 * Restituisco lo MSchema corrispondente 
	 * @param schemaName nome dello schema
	 * @return
	 */
	public MSchema getMetaSchemaFromName(String schemaName){
		return this.schemaList.get(schemaName);		
	}

	/**
	 * Valido una theoria, nel caso non ci siano informazioni su di essa le creo
	 * @param currentTheory
	 * @throws PSQLException 
	 */
	public void validate(String schemaShortFileName,Theory th) throws PSQLException {
		
		//lo eseguo lo stesso nel caso di metabase malformattato o incompleto!
		
		MSchema mSchema = null;
		boolean create = false;
		if ( this.schemaFiles.containsKey(schemaShortFileName) ) {
			mSchema = this.getMetaSchemaFromFilename(schemaShortFileName);
			if ( null == mSchema ) create = true;
		} else {
			create = true;
		}
		
		if ( create ) {
			String[] temp = schemaShortFileName.split("\\.");
			String schemaName = temp[0];
			this.schemaFiles.put(schemaShortFileName, schemaName);
			mSchema = new MSchema(schemaName);
			this.schemaList.put(schemaName, mSchema);
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
	        		
	        		if ( s.getName().equalsIgnoreCase("metabase") ) { //skip lo faccio gia con selfAdd
	        			continue;
	        		}
	        		
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

	public String getName() {
		File f = new File(this.metabaseFile);
		return f.getParentFile().getAbsolutePath();
	}
	
}
