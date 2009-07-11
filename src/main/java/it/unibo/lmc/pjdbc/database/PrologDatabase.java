
package it.unibo.lmc.pjdbc.database;

import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.core.Catalog;
import it.unibo.lmc.pjdbc.database.core.PCatalog;
import it.unibo.lmc.pjdbc.database.core.PSchema;
import it.unibo.lmc.pjdbc.database.core.SCatalog;
import it.unibo.lmc.pjdbc.database.executor.ExecuteControl;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.transaction.TSchema;
import it.unibo.lmc.pjdbc.database.transaction.TSchemaRU;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.ParseException;
import it.unibo.lmc.pjdbc.parser.Psql;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Drop;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;
import it.unibo.lmc.pjdbc.parser.schema.Table;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Database scritto in Prolog
 * 
 * 
 * 
 * 
 * @author yoghi
 *
 */
public class PrologDatabase {
	
	
	/*
	 * TABLE_TYPE String => table type. 
	 * Typical types are "TABLE", "VIEW",	
	 * "SYSTEM TABLE", "GLOBAL TEMPORARY", 
	 * "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 * 
	 * Io uso SystemTable e Table
	 */
	
	/**
	 * Catalog di base
	 */
	private PCatalog baseCatalog;
	
	/**
	 * Catalog di sistema
	 */
	private SCatalog systemCatalog;
	
	/**
	 * Logger 
	 */
	private Logger log = null;
		
	/**
	 * Parser
	 */
	private Psql parse = null;
	
	/**
	 * Control Executors
	 */
	private ExecuteControl control;
	
	/**
	 * Costruttore
	 * @param url	
	 * @param extension 
	 * @throws IOException
	 * @throws PSQLException 
	 */
	public PrologDatabase(String catalogUrl, String defaultSchema) throws IOException, PSQLException {
		
		log = Logger.getLogger(PrologDatabase.class);
		
		control = new ExecuteControl();
		
		log.info("URL : " + catalogUrl);
		
		File f = new File(catalogUrl);
		
		if ( f.isDirectory() ){	//catalog
			
			log.debug("carico catalog "+f.getName());

			this.systemCatalog = new SCatalog(this);
			this.baseCatalog = new PCatalog("base",this);
			
			this.loadSchemas(this.systemCatalog,f,"dbs");
			this.loadSchemas(this.baseCatalog,f,"db");	
			
		} else {	//catalog-file
			
			if ( f.getName().endsWith(".db") ) {
				
				//this.currentCatalogInfo = new MCatalog();
				this.systemCatalog = new SCatalog(this);
				this.baseCatalog = new PCatalog("base",this);
				//this.baseCatalog.addSchema(f.getParent(),f.getName());
				//TODO da fare l'aggiunta dello schema...
			}
			
		}
		
		if (  null != defaultSchema ){
			this.baseCatalog.setCurrentSchemaName(defaultSchema);
		}
		
		log.info("current schema : "+this.baseCatalog.getCurrentSchemaName());
		
	}
	
	/**
	 * Carico gli schema di una catalog dir
	 * @param catalog 
	 * @param f
	 * @throws PSQLException
	 */
	protected void loadSchemas(Catalog catalog, File f,String extension) throws PSQLException{
		
		String dirpath = f.getAbsolutePath();
		String[] children = f.list();	// Get filename of file or directory
		
		for (int i=0; i<children.length; i++) {
            
            String filename = children[i];	
			
			if ( new File(dirpath + File.separator + filename).isFile() ) {

				if ( filename.endsWith(extension.toLowerCase()) ) {
					
					String nameSchema = filename.replace(extension, "");
					PSchema p = new PSchema(dirpath + File.separator + filename,nameSchema);
					TSchemaRU tschema = new TSchemaRU(this,p); 						//TODO instanziare TSChema corretto in base alle politiche attuali...

					catalog.addSchema(tschema,nameSchema);
				} 

			}
        } //for
	}
	
	/**
	 * Parso l'sql e ottengo un oggetto descrittivo
	 * @param sql 
	 * @param schemaName nome dello schema principale 
	 * @return @see ParsedCommand
	 * @throws PSQLException
	 */
	protected ParsedCommand analizeSql(String sql) throws PSQLException{
		
		log.info("query: \""+sql+"\"");
		
		StringReader currentQuery = new StringReader(sql);
		
		if ( null == this.parse ) {
			this.parse = new Psql(currentQuery);
		} else {
			this.parse.ReInit(currentQuery);
		}
		
		ParsedCommand pRequest = null;
		
		try {
			pRequest = parse.parseIt();
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage());
			throw new PSQLException(e.getMessage(),PSQLState.SYNTAX_ERROR);
		}
		return pRequest;
	}
	
	/**
	 * Eseguo una select su uno specifico schema del database
	 * @param sql
	 * @param schemaName
	 * @return
	 * @throws SQLException
	 */
	public PResultSet executeSelect(String sql) throws PSQLException {
		
		ParsedCommand pRequest = this.analizeSql(sql);

		if ( pRequest instanceof Select ) {
			
			Select selectReq = ((Select)pRequest);
			
			String schema = selectReq.getFromTable().get(0).getSchemaName();	//lo schema può essere anche diverso da quello corrente!
			
			if ( schema == null ) schema = this.baseCatalog.getCurrentSchemaName();
			
			log.debug("uso lo schema : "+schema);
			
			for(Table t : selectReq.getFromTable()){
				if ( t.getSchemaName() != null && !t.getSchemaName().equalsIgnoreCase(schema) ) {
					/**
					 * MULTI-SCHEMA
					 * dovrei prendere più lock e poi fare la join delle due theory prima di tutto!
					 */
					throw new PSQLException("not implemented yet!",PSQLState.NOT_IMPLEMENTED);
				}
			}
			
			TSchema tschema;
			if ( baseCatalog.contains(schema) ) {
				tschema = this.baseCatalog.getSchema(schema);
			} else {
				tschema = this.systemCatalog.getSchema(schema);
			}
			
			if ( null != tschema ){
				return tschema.applyCommand( selectReq );
			} else {
				throw new PSQLException("Invalid Schema : "+schema,PSQLState.SYNTAX_ERROR);
			}

		} else throw new PSQLException("Invalid Select : "+pRequest.toString(),PSQLState.DATA_TYPE_MISMATCH);
		
	}
	
	/**
	 * Eseguo un aggiornamento sullo schema indicato
	 * @param sql richiesta sql
	 * @param schemaName
	 * @return numero di righe aggiornate
	 * @throws PSQLException
	 */
	public int executeUpdate(String sql) throws PSQLException {
		
		ParsedCommand pRequest = this.analizeSql(sql);
		
		if ( pRequest instanceof Update ) {	//AGGIORNO UNA RIGA 
			
			Update updateReq = ((Update)pRequest);
			
			String schema = updateReq.getTable().getSchemaName();
			
			if ( null == schema ) {
				schema = this.baseCatalog.getCurrentSchemaName();
			}
			
			TSchema tschema;
			if ( baseCatalog.contains(schema) ) {
				tschema = this.baseCatalog.getSchema(schema);
			} else {
				tschema = this.systemCatalog.getSchema(schema);
			}
			
			if ( null != tschema  ){
				PResultSet res = tschema.applyCommand( updateReq );
				return Integer.parseInt(res.getValue(0).toString());
			} else {
				throw new PSQLException("Invalid Schema : "+schema,PSQLState.SYNTAX_ERROR);
			}

		} 
		
		if ( pRequest instanceof Insert ) { //INSERT new row
			
			Insert insertReq = (Insert)pRequest;
			
			String schema = insertReq.getTable().getSchemaName();
			
			if ( null == schema ) {
				schema = this.baseCatalog.getCurrentSchemaName();
			}
			
			TSchema tschema;
			if ( baseCatalog.contains(schema) ) {
				tschema = this.baseCatalog.getSchema(schema);
			} else {
				tschema = this.systemCatalog.getSchema(schema);
			}
			
			if ( null != tschema  ){
				PResultSet res = tschema.applyCommand( insertReq );
				return Integer.parseInt(res.getValue(0).toString());
			} else {
				throw new PSQLException("Invalid Schema : "+schema,PSQLState.SYNTAX_ERROR);
			}
			
		}
		
		if ( pRequest instanceof Delete ) { //DELETE ROW FROM TABLE
			
			Delete deleteReq = ((Delete)pRequest);
			
			String schema = deleteReq.getFromTable().get(0).getSchemaName();
			
			if ( null == schema ) {
				schema = this.baseCatalog.getCurrentSchemaName();
			}
			
			TSchema tschema;
			if ( baseCatalog.contains(schema) ) {
				tschema = this.baseCatalog.getSchema(schema);
			} else {
				tschema = this.systemCatalog.getSchema(schema);
			}
			
			if ( null != tschema  ){
				PResultSet res = tschema.applyCommand( deleteReq );
				return Integer.parseInt(res.getValue(0).toString());
			} else {
				throw new PSQLException("Invalid Schema : "+schema,PSQLState.SYNTAX_ERROR);
			}
			
		}
		
		if ( pRequest instanceof Drop ) {	// RIMUOVO UNA TABELLA
			
			Drop pDropRequest = (Drop)pRequest;
			
			Set<String> schemaList = pDropRequest.getTablesList().keySet();
			int n = 0;
			
			for (String schema : schemaList) {
			
				TSchema tschema;
				if ( baseCatalog.contains(schema) ) {
					tschema = this.baseCatalog.getSchema(schema);
				} else {
					tschema = this.systemCatalog.getSchema(schema);
				}
				
				if ( null != tschema  ){
					
					PResultSet res = tschema.applyCommand( pDropRequest );
					n += Integer.parseInt(res.getValue(0).toString());
					
				} else {
					throw new PSQLException("Invalid Schema : "+schema,PSQLState.SYNTAX_ERROR);
				}
			
			}
			
			return n;
			
		}
		
		throw new PSQLException("Invalid Update : "+pRequest.toString(),PSQLState.DATA_TYPE_MISMATCH);
		
	}
//	/**

	/**
	 * Ottengo le metainformazioni sullo schema richiesto
	 * @param schemaName nome dello schema
	 */
	public MSchema getMetaSchema(String schemaName){
		return this.systemCatalog.getMSchema(schemaName);
	}
	
	/**
	 * Ottengo lo schema corrente
	 * @return
	 */
	public String getCurrentSchema() {
		return this.baseCatalog.getCurrentSchemaName();
	}
	
	/**
	 * Chiudo il database
	 */
	public void close() {
		this.baseCatalog.close();	//la gestione dei commit in-sospeso è a carico del Pcatalog
		this.systemCatalog.close();
	}

	
	/**
	 * Executor
	 * @return 
	 */
	public ExecuteControl getExecutor() {
		return this.control;	
	}
}
