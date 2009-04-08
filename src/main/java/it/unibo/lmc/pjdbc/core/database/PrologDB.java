package it.unibo.lmc.pjdbc.core.database;

import it.unibo.lmc.pjdbc.core.IDatabase;
import it.unibo.lmc.pjdbc.core.dml.Pselect;
import it.unibo.lmc.pjdbc.driver.PrologMetaData;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;
import it.unibo.lmc.pjdbc.utils.CacheTheoryString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
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
	 * Properties / custumization
	 */
	private Properties properties = new Properties();

	/**
	 * Logger 
	 */
	private Logger log = null;
	
	/**
	 * Reader of prolog file
	 */
	BufferedReader input;
	
	/**
	 * Monitor per coordinare l'accesso al database
	 */
	private ReentrantLock lock = new ReentrantLock();
	
	/**
	 * Database locale scritto in prolog 	
	 * @param sourceUrl path del file contenente il db prolog
	 * @throws IOException
	 * @throws InvalidTheoryException 
	 */
	public PrologDB(String sourceUrl) throws IOException, InvalidTheoryException {
		
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
	    
	    input = new BufferedReader(new FileReader(filePrologDB));
	    
	    File propFile = new File(sourceUrl+".properties");
	    
	    // carico eventuali opzioni
	    if ( propFile.exists() ) {
	    	try {
	    		properties.load(new FileInputStream(propFile));
	    	} catch (Exception e) {
	    		System.out.println("><"+e.getLocalizedMessage());
			}
	    	
	    }
		
		this.logger_init();
		
		this.load_theory();
		
		this.readMeta();
		
	}
	
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
	protected void readMeta() {
		
		PrologMetaData md = new PrologMetaData(this.current_theory);
		
//		Iterator i = this.current_theory.iterator(new Prolog());
//		while(i.hasNext()){
//			Term t = (Term)i.next();
//			if ( t instanceof Struct ){
//	        	
//	        	Struct s = (Struct)t;
//	        	
//	        	// rimane il caso "predicato(...):-!"
//	        	if ( s.isGround() ){
//	        		//NB: X e _ sono due variabili
//	        		//System.out.println("Ground (non contiene variabili) "+x.isGround());	        		
//	        		int l = s.getArity();
//	        		System.out.println(s.getName()+"/"+l);
//	        	}
//	        }
//		}
		
	}
	
	public DatabaseMetaData getMetaData(){
		return null;
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
	 * Inizializzo il sistema di logging
	 */
	protected void logger_init() {
		PropertyConfigurator.configure(properties);
		log = Logger.getLogger("it.unibo.lmc.pjdbc");
	}
	
	/**
	 * Leggo una riga dal database
	 * @param numLine numero di linea da leggere
	 * @return il contenuto della riga o null se la riga non esiste
	 */
	protected String readRow(int numLine) {
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
		
	}
	
	/**
	 * Aggiorno/Inserisco/Rimuovo una riga al database
	 * @param numLine numero di linea
	 * @param newVal il nuovo valore
	 * @return il vecchio valore
	 */
	protected String writeRow(int numLine,String newVal){
		//TODO: da fare..
		return null;
	}
	
	public PrologResultSet applyCommand(Select rsel) {
		
		try {
		
			this.lock.lock();
		
			Pselect result = new Pselect(rsel);
		
			return result.execute(this.current_theory);
			
		} catch (Exception e) {
			e.printStackTrace();
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
