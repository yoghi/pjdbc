package it.unibo.lmc.pjdbc.core.database;

import it.unibo.lmc.pjdbc.core.IDatabase;
import it.unibo.lmc.pjdbc.core.dml.Pselect;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.utils.CacheTheoryString;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;

public abstract class PrologDB implements IDatabase {

	/**
	 * Theory corrente
	 */
	protected Theory current_theory;
	
	/**
	 * Cache della theory sottoforma di stringhe 
	 * in modo da poter riscrivere più veloceente la teoria in caso di insert/update/delete 
	 */
	protected CacheTheoryString cache = new CacheTheoryString();
	
	/**
	 * Monitor per coordinare l'accesso al database
	 */
	private ReentrantLock lock = new ReentrantLock();
	
	/**
	 * Carico la teoria/db prolog  
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidTheoryException
	 */
	protected void load_theory() throws FileNotFoundException, IOException, InvalidTheoryException {
		
		int i = 0;
		String line = this.readRow(i);
		while(line != null ){
			i++;
			this.cache.add(line);
			line = this.readRow(i);
		}
		
		Theory th = new Theory(this.cache.toString());
	
		Prolog engine = new Prolog();
		
		engine.setTheory(th);
		
		this.current_theory = th;
		
		this.cache.show(System.out);	/** @todo da cavare ?? .*/
		
//		try {
//			this.databaseMetaData = new PrologMetaData(dbengine);
//		} catch (Exception e) {
//			this.log.info(e);
//			//throw new SQLException("metabase not present");
//		}
		
	}
	
	/**
	 * Genero i metadati del database
	 */
	@SuppressWarnings("unchecked")
	protected void readMeta() {
		
		Iterator i = this.current_theory.iterator(new Prolog());
		while(i.hasNext()){
			Term t = (Term)i.next();
			if ( t instanceof Struct ){
	        	
	        	Struct s = (Struct)t;
	        	
	        	// rimane il caso "predicato(...):-!"
	        	if ( s.isGround() ){
	        		//NB: X e _ sono due variabili
	        		//System.out.println("Ground (non contiene variabili) "+x.isGround());	        		
	        		int l = s.getArity();
	        		//System.out.println(s.getName()+"/"+l);
	        	}
	        }
		}
		
	}
	
	public IDatabase getSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean joinSnapshot(IDatabase snapshot) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Inizializzo il sistema di log
	 */
	abstract protected void logger_init();
	
	/**
	 * Leggo una riga dal database
	 * @param numLine numero di linea da leggere
	 * @return il contenuto della riga o null se la riga non esiste
	 */
	abstract protected String readRow(int numLine);
	
	/**
	 * Aggiorno/Inserisco/Rimuovo una riga al database
	 * @param numLine numero di linea
	 * @param newVal il nuovo valore
	 * @return il vecchio valore
	 */
	abstract protected String writeRow(int numLine,String newVal);
	
	public PrologResultSet applyCommand(Select rsel) {
		
		try {
		
			this.lock.lock();
		
			Pselect result = new Pselect(rsel);
		
			return result.execute(this.current_theory);
			
		} catch (Exception e) {
			
		} finally {
			this.lock.unlock();
		}
		
		return null;
		
	}
	
	public int applyCommand(Insert rins) {
		return -1;
	}
	
	public int applyCommand(Update rup) {
		return -1;
	}

	public int applyCommand(Delete rdel) {
		return -1;
	}

}
