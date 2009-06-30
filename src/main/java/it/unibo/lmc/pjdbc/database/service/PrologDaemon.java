package it.unibo.lmc.pjdbc.database.service;

import it.unibo.lmc.pjdbc.database.PrologDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class PrologDaemon {
	
	/**
	 * TODO: dovrei controllare se le proprietà del sistema di log sono stati correttamente settati!!!
	 * import org.apache.log4j.PropertyConfigurator;
	 */

	static HashMap<String, PrologDatabase> attivi = new HashMap<String, PrologDatabase>();
	
	//TODO MONITOR da usare per le transizioni.....
	//static 
	
	public static PrologDatabase openDatabase(String url) throws FileNotFoundException, IOException{
		
		/**
		 * TODO: capire come è url e come è la key da usare per memorizzare il database
		 * 
		 * TODO: che transizione uso???
		 */
		
		String key = "";
		
		PrologDatabase p;
		if ( !attivi.containsKey(key) ){
			p = PrologDatabase.getInstance(url);
			attivi.put(key, p);
		}
		
		p = attivi.get(key);
			
		return p;	
	}
	
	public static void close(String url) {
		
		/**
		 * TODO: capire come è url e come è la key da usare per memorizzare il database
		 */
		
		String key = "";
		
		PrologDatabase p = attivi.get(key);
		
		if ( p != null ) {
			
			/**
			 * TODO: Come chiudo il database/schema??
			 */
			
			p.close();
		}
		
		
	}
	
	
	public static void start(String configFile){
		
		/**
		 * Nel config file c'è la directory dove sono localizzati i database
		 * e la cartella dove salvare i file di log delle transazioni
		 * ....
		 */
		
		/**
		 * Avvio Daemon
		 */
		
	}
	
	public static void stop(){
		
		/**
		 * Chiudo Daemon 
		 */
		
		/**
		 * Chiudo i database e sistemo le transazioni in corso... 
		 */
		
	}

	
}
