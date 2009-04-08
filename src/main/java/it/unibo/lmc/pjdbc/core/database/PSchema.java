package it.unibo.lmc.pjdbc.core.database;

import it.unibo.lmc.pjdbc.core.dml.Pselect;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import alice.tuprolog.Theory;

public class PSchema {
	
	/**
	 * Theory corrente
	 */
	protected Theory current_theory;

	/**
	 * Logger 
	 */
	private Logger log = null;

	/**
	 * 
	 * @param sourceUrl path del file contenente il db prolog
	 * @throws FileNotFoundException,IOException 
	 */
	public PSchema(String sourceUrl) throws FileNotFoundException,IOException {
		
		//TODO sistemare l'url... come deve essere...
		
		File filePrologDB = new File(sourceUrl);
		boolean exists = filePrologDB.exists();
	    
	    if ( exists ) {
	    	String userDir = System.getProperty("user.dir");
	    	if ( !(new File(userDir+File.separator+sourceUrl)).exists() ){
	    		throw new FileNotFoundException("File "+sourceUrl+" and "+userDir+File.separator+sourceUrl+" doesn't exist");
	    	} else {
	    		sourceUrl = userDir + File.separator + sourceUrl;
	    	}
	    } else {
	    	System.out.println("Impossibile caricare il db: "+filePrologDB.getAbsolutePath());
	    	boolean success = filePrologDB.createNewFile();
	    	if ( success ) System.out.println("Creato database vuoto: "+filePrologDB.getAbsolutePath());
	    	else throw new IOException("Impossibile creare: "+filePrologDB.getAbsolutePath());
	    }
	    
	    
		this.logger_init();
		
//		this.load_theory();
//		
//		this.load_meta();
//		
	}
	
	/**
	 * Inizializzo il sistema di logging
	 */
	protected void logger_init() {
		log = Logger.getLogger("it.unibo.lmc.pjdbc");
	}
	
	// devo avere informazioni su come è fatto il database
	
	// deve tenere traccia delle transazioni
	
	// eseguire comandi sul database
	
	
	PrologResultSet applyCommand(Select request) throws SQLException {
		
		try {
			
			//this.lock.lock();
		
			Pselect result = new Pselect(request);
		
			return result.execute(this.current_theory);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//this.lock.unlock();
		}
		
		return null;
		
	}
	
	int applyCommand(Insert request) throws SQLException {
		return 0;
	}
	
	int applyCommand(Update request) throws SQLException {
		return 0;
	}
	
	int applyCommand(Delete request) throws SQLException {
		return 0;
	}
	
	
}
