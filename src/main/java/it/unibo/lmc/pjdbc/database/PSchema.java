package it.unibo.lmc.pjdbc.database;

import it.unibo.lmc.pjdbc.database.command.ICommnad;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.command.ddl.PDrop;
import it.unibo.lmc.pjdbc.database.command.dml.PDelete;
import it.unibo.lmc.pjdbc.database.command.dml.PInsert;
import it.unibo.lmc.pjdbc.database.command.dml.PUpdate;
import it.unibo.lmc.pjdbc.database.command.dml.Pselect;
import it.unibo.lmc.pjdbc.database.meta.MCatalog;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Drop;
import it.unibo.lmc.pjdbc.parser.dml.imp.DropDB;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;

public class PSchema implements ICommnad {
	
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
	 * @param catalogSchema catalog meta informazioni
	 * @throws FileNotFoundException,IOException 
	 * @throws  
	 */	
	public PSchema(String sourceUrl, MCatalog catalogSchema) throws PSQLException {
	
		File filePrologDB = new File(sourceUrl);
		boolean exists = filePrologDB.exists();
	    
		this.logger_init();
		
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
			
			catalogSchema.validate(filePrologDB.getName(),this.current_theory);
			
			if ( catalogSchema.existMetaSchema(filePrologDB.getName()) ){
				this.metaSchema = catalogSchema.getMetaSchema(filePrologDB.getName());
			} else {
				//qui non ci posso essere se validate ha funzionato....
				log.error("sono dove non dovrei essere...");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log.info("Schema ready: " + this.metaSchema.getSchemaName());
	}
	
	/**
	 * Inizializzo il sistema di logging
	 */
	protected void logger_init() {
		log = Logger.getLogger(PSchema.class);
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
	
	

	public PResultSet applyCommand(Select request) throws PSQLException {
		
		Pselect prq = new Pselect(this.metaSchema,request);
		String gen_psql = prq.generatePrologRequest();
		
		log.debug("psql da eseguire: "+gen_psql);

		List<Term[]> rows = new Vector<Term[]>();
		List<TableField> fields = request.getCampiRicerca();	// il campo alias è il campo con cui si presentano nella SELECT (e.di,$1,etc...)
		
		//fields se contiene * va convertito
		
		try {
		
			Prolog p = new Prolog();
			
			p.setTheory(this.current_theory);
			
			SolveInfo info = p.solve(gen_psql);
			
			while (info.isSuccess()){ 
				
				log.debug("soluzione"+info.getBindingVars().toString());
				
				Term[] row = new Term[fields.size()];
				int i=0;
				
				for (TableField field : fields) {
					
					String fieldName = field.getQualifiedName();
					String varName = prq.sql2prologVar(fieldName);
					Term t = info.getVarValue(varName);
					row[i] = t;
					i++;
				}
				
				rows.add(row);
				
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
	
		} catch (InvalidTheoryException e) {
			throw new PSQLException(e.getLocalizedMessage(), PSQLState.SYSTEM_ERROR);
		} catch (MalformedGoalException e) {
			throw new PSQLException(e.getLocalizedMessage(), PSQLState.SYSTEM_ERROR);
		} catch (NoSolutionException e) {
			// non ho soluzionio 
		}
		
		return new PResultSet(fields,rows,this);

	}

	public int applyCommand(Insert request) throws PSQLException {
		
		/**
		 * Assert
		 */
		
		PInsert prq = new PInsert(this.metaSchema,request);

		try {
			
			Prolog p = new Prolog();
			p.setTheory(this.current_theory);
			
			String requestPsql = prq.generatePrologRequest();
			
			log.debug("psql da eseguire su "+this.metaSchema.getSchemaName()+" : "+requestPsql);
			
			SolveInfo info = p.solve(requestPsql);
			
			log.debug(info.toString());
			
			if ( info.isSuccess() ) {
				this.current_theory = p.getTheory(); //TODO: manca la questione del salvataggio in uscita...
				
				System.out.println(this.current_theory.toString());
				
				return 1;
			}
			else {
				log.error("errore");
				throw new PSQLException("", PSQLState.SYSTEM_ERROR);
			}
 			
		} catch (InvalidTheoryException e) {
			
		} catch (MalformedGoalException e) {
			
		}
		
		return 0;
	}
	
	public int applyCommand(Update request) throws PSQLException {
		
		/**
		 * Retract => select delle righe che matchano con la where e loro rimozione
		 * +
		 * Assert => delle righe con la modifica
		 */
		
		PUpdate updateReq = new PUpdate(this.metaSchema, request);
		
		try {
		
			Prolog p = new Prolog();
			p.setTheory(this.current_theory);
			
			String requestPsql = updateReq.generatePrologRequest();
			
			log.debug("psql da eseguire su "+this.metaSchema.getSchemaName()+" : "+requestPsql);
			
			SolveInfo info = p.solve(requestPsql);
			
			log.debug(info.toString());
			
			int n = 0;
			while ( info.isSuccess() ) {
				n++;
				
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
			
			this.current_theory = p.getTheory(); //TODO: manca la questione del salvataggio in uscita...
			
			System.out.println(this.current_theory);
			
			return n;
		
		} catch (InvalidTheoryException e) {
			
		} catch (MalformedGoalException e) {
			
		}
		
		return 0;
	}
	
	public int applyCommand(Delete request) throws PSQLException {
		
		/**
		 * Retract => select delle righe che matchano con la where e loro rimozione
		 */
		
		PDelete deleteReq = new PDelete(this.metaSchema,request);

		try {
			
			Prolog p = new Prolog();
			p.setTheory(this.current_theory);
			
			String requestPsql = deleteReq.generatePrologRequest();
			
			log.debug("psql da eseguire su "+this.metaSchema.getSchemaName()+" : "+requestPsql);
			
			SolveInfo info = p.solve(requestPsql);
			
			log.debug(info.toString());
			
			int n = 0;
			while ( info.isSuccess() ) {
				n++;
				
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
			
			this.current_theory = p.getTheory(); //TODO: manca la questione del salvataggio in uscita...
			
			return n;
 			
		} catch (InvalidTheoryException e) {
			
		} catch (MalformedGoalException e) {
			
		}
		
		
		
		return 0;
	}
	
	public int applyCommand(Drop request) throws PSQLException {
		
		/**
		 * Retract
		 */
		
		PDrop dropReq = new PDrop(this.metaSchema, request);
		
		try {
			
			Prolog p = new Prolog();
			p.setTheory(this.current_theory);
			
			String requestPsql = dropReq.generatePrologRequest();
			
			log.debug("psql da eseguire su "+this.metaSchema.getSchemaName()+" : "+requestPsql);
			
			SolveInfo info = p.solve(requestPsql);
			
			log.debug(info.toString());
			int n = 0;
			while ( info.isSuccess() ) {
				n++;
				
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
			
			this.current_theory = p.getTheory(); //TODO: manca la questione del salvataggio in uscita...
			
			return n;
 			
		} catch (InvalidTheoryException e) {
			
		} catch (MalformedGoalException e) {
			
		}
		
		
		return 0;
	}

	public void applyCommand(DropDB request) throws PSQLException {
		throw new PSQLException("non implemented yet", PSQLState.NOT_IMPLEMENTED);
	}
	
	public void close() {
		//TODO: devo rilasciare le risorse : theory e quant'altro a esso collegata
	}
	
	/**
	 * Restituisco i metadati
	 * @return
	 */
	protected MSchema getSchemaInfo() {
		return this.metaSchema;
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

	
	
}
