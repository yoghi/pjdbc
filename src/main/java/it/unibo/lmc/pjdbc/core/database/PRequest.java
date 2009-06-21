package it.unibo.lmc.pjdbc.core.database;

import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

public abstract class PRequest {

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
	 * Var : (k)Psql to Sql 
	 */
	protected Hashtable<String,String> mapVariables = new Hashtable<String, String>();
	
//	/**
//	 * Clausole PSQL
//	 * 	tabella1 => campo1,campo2,campo3
//	 *  tabella2 => campo1
//	 *  clausola 1 => null 
//	 */
//	private HashMap<String,String[]> clausolePsql; 
	
//	/**
//	 * Aggiungo una clausola a quelle da eseguire
//	 * @param clausola
//	 */
//	public void AND(String clausola) {
//		if ( null == this.clausole ) this.clausole = clausola;
//		else this.clausole += ","+clausola;
//	}
	
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
	 * Trasforma la var usata in sql in quella usate nel prolog
	 * @param var
	 * @return
	 */
	public String getVarAliasSqltoProlog(String var){
		if ( null != this.mapVariables ) {
			
			if ( !var.contains(".") ){
			
				//posso aver inserito direttamente un alias
				// oppure
				//ho omesso il fatto che è un campo della prima tabella utilizzata
				String table_name = (String)this.aliasTables.values().toArray()[0];
				String var_research = table_name+"."+var;
				
				//controllo se var esiste
				for (String key : this.mapVariables.keySet()) {
					if ( this.mapVariables.get(key).equalsIgnoreCase(var_research) ) return key;
					if ( this.mapVariables.get(key).equalsIgnoreCase(var) ) return key;
				}
				
			} else {
			
				String[] var_research = var.split("\\.");
				String table_name = this.aliasTables.get(var_research[0]);
				String var2 = table_name+"."+var_research[1];
				
				//controllo se var esiste
				for (String key : this.mapVariables.keySet()) {
					if ( this.mapVariables.get(key).equalsIgnoreCase(var2) ) return key;
					if ( this.mapVariables.get(key).equalsIgnoreCase(var) ) return key;
				}
				
			}		
			
		}
		return null;
	}

	/**
	 * Valuto la richiesta corrente e ne genero una richiesta Prolog 
	 */
	abstract public String generatePrologRequest() throws SQLException;
	
}
