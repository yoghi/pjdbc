package it.unibo.lmc.pjdbc.database.command;

import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

import java.util.Hashtable;

import org.apache.log4j.Logger;

public abstract class PRequest {
	
	private int countVar = 0;

	/**
	 * Logger di sistema
	 */
	protected Logger log;
	
	/**
	 * Metadati sullo schema corrente
	 */
	protected MSchema mschema;
	
	/**
	 * Metadati della richiesta
	 */
	protected ParsedCommand mcommand;
	
	/**
	 * Clausole della richiesta
	 * 
	 * 	funtore clausola => clausola 
	 * 
	 */
	protected Hashtable<String,PClausola> clausole = new Hashtable<String, PClausola>();
	
	/**
	 * Var : (k)Psql to Sql 
	 */
	protected Hashtable<String,String> mapVariables = new Hashtable<String, String>();
	
	/**
	 * Costruttore 
	 * @param ms sono i metadati dello schema su cui si lavora
	 * @param req sono le metainformazioni sul comando che si deve eseguire
	 */
	public PRequest(MSchema ms, ParsedCommand req) {
		this.mschema = ms;
		this.mcommand = req;
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");	//forzo in modo da poter riunire tutte le dml request in un solo log
	}

	/**
	 * Genero una nuova variabile da usare nelle clausole
	 * @return variabile
	 */
	protected String generateNewVar(String sqlVar){
		
		String psql_var = "IMP"+this.countVar+"VAR";	//TODO: che metto come valore nella clausola?? che incognita metto??
		
		String key = this.sql2prologVar(sqlVar);
		
		if ( null != key ) {
			log.warn("variabile sql : "+sqlVar+" già assegnata una volta");
			return key;
		}
		log.debug("associo "+psql_var+ " a "+sqlVar);
		this.mapVariables.put(psql_var, sqlVar); 
		countVar++;
		return psql_var;
	}
	
	/**
	 * Converto una variabile sql nella sua controparte prolog
	 * @param sqlVar variabile sql
	 * @return variabile prolog
	 */
	public String sql2prologVar(String sqlVar){
		if ( this.mapVariables.containsValue(sqlVar) ){
			for (String key : this.mapVariables.keySet()) {
				if ( this.mapVariables.get(key).equalsIgnoreCase(sqlVar) ){
					return key;
				}
			}
		}
		return null;
	}
	
	/**
	 * Converto una variabile prolog nella sua controparte sql
	 * @param prologVar variabile prolog
	 * @return variabile sql
	 */
	public String prolog2sqlVar(String prologVar){		
		return this.mapVariables.get(prologVar);
	}
	
	
	/**
	 * Valuto la richiesta corrente e ne genero una richiesta Prolog 
	 */
	abstract public String generatePrologRequest() throws PSQLException;
	
}
