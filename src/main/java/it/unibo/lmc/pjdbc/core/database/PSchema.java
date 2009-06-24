package it.unibo.lmc.pjdbc.core.database;

import it.unibo.lmc.pjdbc.core.dml.IDml;
import it.unibo.lmc.pjdbc.core.dml.Pselect;
import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

public class PSchema implements IDml {
	
	/**
	 * Theory corrente
	 */
	protected Theory current_theory;
	
	/**
	 * Metadati dello schema
	 */
	protected MSchema metaSchema;

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

	/**
	 * 
	 * @param sourceUrl path del file contenente il db prolog
	 * @throws FileNotFoundException,IOException 
	 * @throws  
	 */
	public PSchema(String sourceUrl) throws FileNotFoundException,IOException {
		
		//TODO sistemare l'url... come deve essere...
		
		File filePrologDB = new File(sourceUrl);
		boolean exists = filePrologDB.exists();
	    
		this.logger_init();
		
	   if ( exists ) {
		   log.info("Open file : "+filePrologDB.getAbsolutePath());
	   } else {
		   log.info("Impossibile caricare lo schema: "+filePrologDB.getAbsolutePath());
//		   if ( !filePrologDB.createNewFile() ) throw new IOException("Impossibile creare: "+filePrologDB.getAbsolutePath());
//		   log.info("Creato schema vuoto: "+filePrologDB.getAbsolutePath());
	   }
	    
	    this.schemaFile = filePrologDB.getAbsolutePath();
	    
	    this.metaSchema = new MSchema(filePrologDB.getName());	//TODO solo il nome o tutto il direttorio??
	    
		this.load_theory();

		this.load_meta();

	}

	protected PSchema(Theory th, MSchema schema){
		this.current_theory = th;
		this.logger_init();
		this.metaSchema = schema;
	}
	
	public PSchema clone(){
		return new PSchema(this.current_theory,this.metaSchema);
	}
	
	/**
	 * Inizializzo il sistema di logging
	 */
	protected void logger_init() {
		log = Logger.getLogger(PSchema.class);
	}
	
	/**
	 * Carico la teoria prolog
	 * @throws IOException 
	 */
	protected void load_theory() throws IOException  {
		
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
	
	protected void load_meta() {
		this.metaSchema.loadFromTheory(this.current_theory);
	}

	public PrologResultSet applyCommand(Select request) throws SQLException {
		
		Pselect prq = new Pselect(this.metaSchema,request);

		String gen_psql = prq.generatePrologRequest();
		
		log.debug("psql da eseguire: "+gen_psql);

		List<PSolution> rows = new ArrayList<PSolution>();
		
		try {
		
			Prolog p = new Prolog();
			
			p.setTheory(this.current_theory);
			
			SolveInfo info = p.solve(gen_psql);
			
			while (info.isSuccess()){ 
				
				log.debug(info.getBindingVars().toString());
				
				rows.add(new PSolution(info));
				
				if (p.hasOpenAlternatives()){ 
					try {
						info=p.solveNext();
					} catch (NoMoreSolutionException e) {
						break;
					} 
				} else { 
					break;
				}
				
			}
			
			return new PrologResultSet(rows,prq);
	
		} catch (InvalidTheoryException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		} catch (MalformedGoalException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		} catch (NoSolutionException e) {
			// non ho soluzionio 
			return new PrologResultSet(rows,prq);
		}

	}

	public int applyCommand(Insert request) throws SQLException {
		
		/**
		 * Assert
		 */
		
		return 0;
	}
	
	public int applyCommand(Update request) throws SQLException {
		
		/**
		 * Retract => select delle righe che matchano con la where e loro rimozione
		 * +
		 * Assert => delle righe con la modifica
		 */
		
		return 0;
	}
	
	public int applyCommand(Delete request) throws SQLException {
		
		/**
		 * Retract
		 */
		
		return 0;
	}

	public void close() {
		//TODO: devo rilasciare le risorse : theory e quant'altro a esso collegata
	}
	
	
}
