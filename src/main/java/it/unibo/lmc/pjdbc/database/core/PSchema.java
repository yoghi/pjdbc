package it.unibo.lmc.pjdbc.database.core;

import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
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
	 * Quanto può durare al massimo l'esecuzione di una istruzione
	 */
	protected Long timeout = Long.MAX_VALUE;
	
	/**
	 * Lock per l'accesso allo schema in maniera sicura => i comandi DML sono ATOMICI!!
	 */
	protected ReentrantLock lock = new ReentrantLock();

	/**
	 * File contenente lo schema corrente
	 */
	private String schemaFile;

	private String sName;

	/**
	 * 
	 * @param sourceUrl path del file contenente il db prolog
	 * @param catalogSchema catalog meta informazioni
	 * @throws FileNotFoundException,IOException 
	 * @throws  
	 */	
	public PSchema(String sourceUrl,String name) throws PSQLException {
	
		this.sName = name; 
		
		File filePrologDB = new File(sourceUrl);
		boolean exists = filePrologDB.exists();
	    
		log = Logger.getLogger(PSchema.class);
		
		try {
		
			if (exists) {
				log.info("Open file : " + filePrologDB.getAbsolutePath());
			} else {
				log.info("Impossibile caricare lo schema: " + filePrologDB.getAbsolutePath());
				if (!filePrologDB.createNewFile()) throw new PSQLException("Impossibile creare: "+ filePrologDB.getAbsolutePath(),PSQLState.SYSTEM_ERROR);
				log.info("Creato schema vuoto: " + filePrologDB.getAbsolutePath());
			}
	
			this.schemaFile = filePrologDB.getAbsolutePath();
	
			this.load_theory();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log.info("Loaded schema : " + sourceUrl);
	}
	
	/**
	 * Carico la teoria prolog
	 */
	protected void load_theory()  {
		
		try {
			this.current_theory = new Theory(new FileInputStream(this.schemaFile));
			return;
		} catch (FileNotFoundException e) {
			log.error("Invalid Theory, inizialized current theory to empty!");
		} catch (IOException e) {
			log.error("Invalid Theory, inizialized current theory to empty!");
		}
		
		try {
			this.current_theory = new Theory("");
		} catch (InvalidTheoryException e1) {
			log.error("Cannot inizialize empty theory");
		}
	}
	
	public void close() {
		//TODO: devo rilasciare le risorse : theory e quant'altro a esso collegata
	}

	public void commit() throws PSQLException {
		try {
			FileOutputStream theoryFile = new FileOutputStream(this.schemaFile);
			theoryFile.write( this.current_theory.toString().getBytes());
			theoryFile.flush();
			theoryFile.close();
		} catch (IOException e) {
			throw new PSQLException("Impossibile salvare le modifiche", PSQLState.SYSTEM_ERROR);
		}
	}

	public String getFileName() {
		return this.schemaFile;
	}

	public Theory getTheory() {
		return this.current_theory;
	}	
	
	public void setTheory(Theory newT){
		this.current_theory = newT;
	}

	public String getName() {
		return sName;
	}
	
}
