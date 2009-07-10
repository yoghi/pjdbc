
package it.unibo.lmc.pjdbc.database;

import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.meta.MCatalog;
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
import java.util.Hashtable;
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
	
	/**
	 * Schemi attivi
	 */
	private Hashtable<String,TSchema> availableSchema = new Hashtable<String, TSchema>();
	
	/**
	 * Metabase
	 */
	private MCatalog catalogSchema;
	
	/**
	 * Logger 
	 */
	private Logger log = null;
		
	/**
	 * Parser
	 */
	private Psql parse = null;
	
	/**
	 * Schema corrente
	 */
	private String currentSchema = null;
	
	/**
	 * Costruttore
	 * @param url	
	 * @param extension 
	 * @throws IOException
	 * @throws PSQLException 
	 */
	public PrologDatabase(String catalogUrl, String defaultSchema) throws IOException, PSQLException {
		
		log = Logger.getLogger(PrologDatabase.class);
		
		log.info("URL : " + catalogUrl);
		
		File f = new File(catalogUrl);
		
		if ( f.isDirectory() ){	//catalog
			
			log.debug("carico catalog "+f.getName());
			this.loadCatalog(f);
			this.loadSchemas(f);
			
		} else {	//catalog into file??
			
			if ( f.getName().endsWith(".db") ) {
				this.catalogSchema = new MCatalog();
				this.addSchema(f.getParent(),f.getName());
			} 
			
		}
		
		if (  null != defaultSchema && this.availableSchema.containsKey(defaultSchema) ){
			this.currentSchema = defaultSchema;
		} else {
			this.currentSchema = this.availableSchema.keys().nextElement();
		}
		
		log.info("current schema : "+this.currentSchema);
		
	}

	/**
	 * Carico il catalog da file
	 * @param f
	 * @throws PSQLException
	 */
	protected void loadCatalog(File f) throws PSQLException{
		String dirpath = f.getAbsolutePath();
		this.catalogSchema = new MCatalog( dirpath + File.separator + "metabase.db" );
	}
	
	/**
	 * Carico gli schema di una catalog dir
	 * @param f
	 * @throws PSQLException
	 */
	protected void loadSchemas(File f) throws PSQLException{
		
		String dirpath = f.getAbsolutePath();
		String[] children = f.list();	// Get filename of file or directory
		
		for (int i=0; i<children.length; i++) {
            
            String filename = children[i];	
			
			if ( new File(dirpath + File.separator + filename).isFile() ) {

				if ( filename.endsWith(".db") ) {
					this.addSchema(dirpath,filename);
				} 

			}
        } //for
	}
	
	/**
	 * Aggiungo uno schema nel sistema
	 * @param dirpath
	 * @param filename
	 * @throws PSQLException
	 */
	protected void addSchema(String dirpath,String filename) throws PSQLException{

		PSchema p = new PSchema(dirpath + File.separator + filename,this.catalogSchema);
		
		//TODO instanziare TSChema corretto in base alle politiche attuali...
		TSchemaRU tschema = new TSchemaRU(p);
        
		MSchema mSchema = this.catalogSchema.getMetaSchemaFromFilename(filename);
		String nameSchema = mSchema.getSchemaName();  //filename.split("\\.")[0];
		
		this.availableSchema.put(nameSchema,tschema);
		this.log.info("Caricato schema : "+nameSchema);	
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
			
			String schema;	//lo schema può essere anche diverso da quello corrente!
			if ( pRequest.getSchemaName() != null ) schema = pRequest.getSchemaName();
			else schema = this.currentSchema;
			
			log.debug("uso lo schema : "+schema);
			
			TSchema tschema = this.availableSchema.get(this.currentSchema);
			
			if ( null != tschema  ){
				
				Select selectReq = ((Select)pRequest);

				for(Table t : selectReq.getFromTable()){
					if ( t.getSchemaName() != null && !t.getSchemaName().equalsIgnoreCase(schema) ) {
						/**
						 * MULTI-SCHEMA
						 * dovrei prendere più lock e poi fare la join delle due theory prima di tutto!
						 */
						throw new PSQLException("not implemented yet!",PSQLState.NOT_IMPLEMENTED);
					}
				}
				
				return tschema.applyCommand( selectReq );
			} else {
				throw new PSQLException("Invalid Schema : "+pRequest.getSchemaName(),PSQLState.SYNTAX_ERROR);
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
				if ( pRequest.getSchemaName() != null ) schema = pRequest.getSchemaName();
				else schema = this.currentSchema;
			}
			
			TSchema tschema = this.availableSchema.get(schema);
			
			if ( null != tschema  ){
				
				return tschema.applyCommand( updateReq );
				
			} else {
				throw new PSQLException("Invalid Schema : "+pRequest.getSchemaName(),PSQLState.SYNTAX_ERROR);
			}

		} 
		
		if ( pRequest instanceof Insert ) { //INSERT new row
			
			Insert insertReq = (Insert)pRequest;
			
			String schema = insertReq.getTable().getSchemaName();
			
			if ( null == schema ) {
				if ( pRequest.getSchemaName() != null ) schema = pRequest.getSchemaName();
				else schema = this.currentSchema;
			}
			
			TSchema tschema = this.availableSchema.get(schema);
			
			if ( null != tschema  ){
				
				return tschema.applyCommand( insertReq );
				
			} else {
				throw new PSQLException("Invalid Schema : "+pRequest.getSchemaName(),PSQLState.SYNTAX_ERROR);
			}
			
		}
		
		if ( pRequest instanceof Delete ) { //DELETE ROW FROM TABLE
			
			Delete deleteReq = ((Delete)pRequest);
			
			String schema = deleteReq.getFromTable().get(0).getSchemaName();
			
			if ( null == schema ) {
				if ( pRequest.getSchemaName() != null ) schema = pRequest.getSchemaName();
				else schema = this.currentSchema;
			}
			
			TSchema tschema = this.availableSchema.get(schema);
			
			if ( null != tschema  ){
				return tschema.applyCommand( deleteReq );
			} else {
				throw new PSQLException("Invalid Schema : "+pRequest.getSchemaName(),PSQLState.SYNTAX_ERROR);
			}
			
		}
		
		if ( pRequest instanceof Drop ) {	// RIMUOVO UNA TABELLA
			
			Drop pDropRequest = (Drop)pRequest;
			
			Set<String> schemaList = pDropRequest.getTablesList().keySet();
			int n = 0;
			
			for (String schema : schemaList) {
			
				TSchema tschema = this.availableSchema.get(schema);
				
				if ( null != tschema  ){
					
					n += tschema.applyCommand( pDropRequest );
					
				} else {
					throw new PSQLException("Invalid Schema : "+schema,PSQLState.SYNTAX_ERROR);
				}
			
			}
			
			return n;
			
		}
		
		throw new PSQLException("Invalid Update : "+pRequest.toString(),PSQLState.DATA_TYPE_MISMATCH);
		
	}

	/**
	 * Ottengo le metainformazioni sul catalog corrente
	 * @return
	 */
	public MCatalog getCatalogInfo() {
		return this.catalogSchema;
	}

	/**
	 * Ottengo lo schema corrente
	 * @return
	 */
	public String getCurrentSchema() {
		return currentSchema;
	}
	
	/**
	 * Chiudo il database
	 */
	public void close() {
		//TODO: rilascio le risorse
		
		for(String name : this.availableSchema.keySet()) {
			
			TSchema p = this.availableSchema.get(name);
			p.close();
			this.availableSchema.remove(name);
			this.log.info("Close schema : "+name);
		}
		
	}
}
