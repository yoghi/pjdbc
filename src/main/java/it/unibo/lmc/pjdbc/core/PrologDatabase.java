package it.unibo.lmc.pjdbc.core;

import it.unibo.lmc.pjdbc.core.database.PResultSet;
import it.unibo.lmc.pjdbc.core.database.PSQLState;
import it.unibo.lmc.pjdbc.core.database.PSchema;
import it.unibo.lmc.pjdbc.core.transaction.TSchema;
import it.unibo.lmc.pjdbc.core.transaction.TSchemaRU;
import it.unibo.lmc.pjdbc.parser.ParseException;
import it.unibo.lmc.pjdbc.parser.Psql;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.utils.PSQLException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.util.Hashtable;

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
	 * Ci puo essere un solo PrologDatabase per directory/file
	 */
	static private Hashtable<String,PrologDatabase> instances = new Hashtable<String, PrologDatabase>();
	
	/**
	 * Schemi attivi
	 */
	private Hashtable<String,TSchema> availableSchema = new Hashtable<String, TSchema>();
	
	/**
	 * Logger 
	 */
	private Logger log = null;
	
	
	/**
	 * Parser
	 */
	private Psql parse = null;
	
	/**
	 * Ottengo un PrologDatabase
	 * @param url
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	public static PrologDatabase getInstance(String url,String extension) throws IOException{
		if ( !instances.containsKey(url) ) {
			instances.put(url, new PrologDatabase(url,extension));
		}
		return instances.get(url);
	}
	
	/**
	 * Ottengo un PrologDatabase
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static PrologDatabase getInstance(String url) throws IOException{
		if ( !instances.containsKey(url) ) {
			instances.put(url, new PrologDatabase(url));
		}
		return instances.get(url);
	}
	
	/**
	 * 
	 * @param url
	 * @throws IOException
	 */
	private PrologDatabase(String url) throws IOException {
		this(url,"db");
	}
	
	/**
	 * 
	 * @param url	
	 * @param extension 
	 * @throws IOException
	 */
	private PrologDatabase(String url, String extension) throws IOException {
		
		log = Logger.getLogger(PrologDatabase.class);
		
		log.info("URL : " + url);
		
		File f = new File(url);

		// se il file non esiste lancio IOException... 

		if ( f.isDirectory() ){
			
			String dirpath = f.getAbsolutePath();
			
			String[] children = f.list();
			
			for (int i=0; i<children.length; i++) {
	            
				// Get filename of file or directory
	            String filename = children[i];
				
				if ( new File(dirpath + File.separator + filename).isFile() ) {
		            
					if ( filename.endsWith(extension) ) {
					
						PSchema p = new PSchema(dirpath + File.separator + filename);
						
						//TODO instanziare TSChema corretto in base alle politiche attuali...
						TSchemaRU tschema = new TSchemaRU(p);
			            
						String nameSchema = filename.split("\\.")[0];
						this.log.info("Open schema : "+nameSchema);
						this.availableSchema.put(nameSchema,tschema);
						
					} else {
						log.debug("into dir find file with invalid url - not use extension : "+extension+ " - "+filename);
					}
					
				}
	        }
		} else {
			
			if ( url.endsWith(extension) ) {
				
				PSchema p = new PSchema(url);
				//TODO instanziare TSChema corretto in base alle politiche attuali...
				TSchemaRU tschema = new TSchemaRU(p);
				
				String nameSchema = f.getName().split("\\.")[0];
				this.log.info("Open schema : "+nameSchema);
				this.availableSchema.put(nameSchema,tschema);
				
			} else {
				log.error("invalid url - not use extension : "+extension);
				throw new IOException("invalid url");
			}
			
		}
		
	}

	/**
	 * Chiudo il database
	 */
	public void close() {
		//TODO: rilascio le risorse
		
		for(String name : this.availableSchema.keySet()) {
			
			TSchema p = this.availableSchema.get(name);
			p.commit();
			p.close();
			this.availableSchema.remove(name);
			this.log.info("Close schema : "+name);
		}
		
	}

	/**
	 * Eseguo una select sul default schema del database
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public PResultSet executeSelect(String sql) throws PSQLException {
		String nameSchema = this.availableSchema.keys().nextElement();
		return this.executeSelect(sql,nameSchema);
	}
	
	/**
	 * Eseguo una select su uno specifico schema del database
	 * @param sql
	 * @param schemaName
	 * @return
	 * @throws SQLException
	 */
	public PResultSet executeSelect(String sql, String schemaName) throws PSQLException {
		
		log.info("query: \""+sql+"\"");
		
		StringReader currentQuery = new StringReader(sql);
		
		if ( null == this.parse ) {
			this.parse = new Psql(currentQuery);
		} else {
			this.parse.ReInit(currentQuery);
		}
		
		ParsedCommand pRequest = null;
		
		try {
			pRequest = parse.parseIt(schemaName);
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage());
			throw new PSQLException(e.getMessage(),PSQLState.SYNTAX_ERROR);
		}

		if ( pRequest instanceof Select ) {
			
			TSchema tschema = this.availableSchema.get(schemaName);
			
			if ( null != tschema  ){
				
				Select selectReq = ((Select)pRequest);

				for(Table t : selectReq.getFromTable()){
					if ( ! t.getSchemaName().equalsIgnoreCase(schemaName) ) {
						/**
						 * MULTI-SCHEMA
						 * dovrei prendere più lock e poi fare la join delle due theory prima di tutto!
						 */
						throw new PSQLException("not implemented yet!",PSQLState.NOT_IMPLEMENTED);
					}
				}
				
				return tschema.applyCommand( selectReq );
			} else {
				throw new PSQLException("Invalid Schema : "+schemaName,PSQLState.SYNTAX_ERROR);
			}

		} else throw new PSQLException("Invalid Select : "+pRequest.toString(),PSQLState.DATA_TYPE_MISMATCH);
		
	}
	
	
	
}
