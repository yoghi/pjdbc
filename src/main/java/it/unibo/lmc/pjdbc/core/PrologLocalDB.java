package it.unibo.lmc.pjdbc.core;

import it.unibo.lmc.pjdbc.core.request.IRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;

/**
 * Database Prolog
 * In questo classe ho 
 * 		un meccanismo di lock per garantire il corretto accesso alla risorsa
 * 		uno di commit per la conferma dei cambiamenti
 * 		uno di savePoint  per poter salvare lo stato in un dato momento 
 */
public class PrologLocalDB implements IDatabase {
	
	/**
	 * Properties / custumization
	 */
	private Properties properties = new Properties();;

	/**
	 * Logger 
	 */
	private Logger log = null;
	
	/**
	 * Lock 
	 */
	ReentrantLock lock = new ReentrantLock();
	
	/**
	 * Database name
	 */
	String schema;
	
	/**
	 * Theory corrente
	 */
	Theory current_theory;
	
	/** 
	 * 
	 * @todo: dovrei poter salvare gli stati su file e non solo su memoria 
	 */
	HashMap<String, Theory> savePoint = new HashMap<String, Theory>();
	
	/**
	 * Database locale scritto in prolog 	
	 * @param sourceUrl path del file contenente il db prolog
	 * @throws IOException
	 * @throws InvalidTheoryException 
	 */
	public PrologLocalDB(String sourceUrl) throws IOException, InvalidTheoryException {
		
		File file = new File(sourceUrl);
		boolean exists = file.exists();
	    
	    if ( exists ) {
	    	String userDir = System.getProperty("user.dir");
	    	if ( !(new File(userDir+File.separator+sourceUrl)).exists() ){
	    		throw new FileNotFoundException("File "+sourceUrl+" and "+userDir+File.separator+sourceUrl+" doesn't exist");
	    	} else {
	    		sourceUrl = userDir + File.separator + sourceUrl;
	    	}
	    } else {
	    	System.out.println("Impossibile caricare il db: "+file.getAbsolutePath());
	    	boolean success = file.createNewFile();
	    	if ( success ) System.out.println("Creato database vuoto: "+file.getAbsolutePath());
	    	else throw new IOException("Impossibile creare: "+file.getAbsolutePath());
	    }
	    
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
		
		this.load_theory(file);
		
		this.createMeta();
		
	}
	
	/**
	 * Carico la teoria/db da un file
	 * @param file file da cui caricare la teoria
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidTheoryException
	 */
	private void load_theory(File file) throws FileNotFoundException, IOException, InvalidTheoryException {
		
		this.log.debug("Avvio prolog db engine");

		Theory th = new Theory(new FileInputStream(file));
		
		this.log.debug("Verifico la teoria prolog");
		
		Prolog engine = new Prolog();
		
		engine.setTheory(th);
		
		this.current_theory = th;
		
//		try {
//			this.databaseMetaData = new PrologMetaData(this.dbengine);
//		} catch (Exception e) {
//			this.log.info(e);
//			//throw new SQLException("metabase not present");
//		}
		
	}

	/**
	 * Inizializzo il sistema di logging
	 */
	protected void logger_init() {
		PropertyConfigurator.configure(properties);
		log = Logger.getLogger("it.unibo.lmc.pjdbc");
	}
	
	
	

	/**
	 * Eseguo una richiesta sul database
	 * @param request
	 * @return Lista contenente tutti i {@link SolveInfo}
	 */
	public ArrayList<SolveInfo> executeRequest(IRequest request){
	
		ArrayList<SolveInfo> lista = new ArrayList<SolveInfo>();
		
		try {
			
			while(!lock.tryLock()){}
				
			/** eseguo la richiesta */
			try {
				
				Prolog engine = new Prolog();
				
				engine.setTheory(this.current_theory);
				
				SolveInfo s = engine.solve(request.toString());
				
				while(s.isSuccess()){
					lista.add(s);
					if ( engine.hasOpenAlternatives() ) {
						s = engine.solveNext();
					} else {
						break;
					}
				}
				
				this.current_theory = engine.getTheory();
				this.createMeta();
				
			} catch (InvalidTheoryException e) {
				/* ... impossibile ... */
				
			} catch (MalformedGoalException e) {
				
				/** @todo: gestire la eccezzione, vuol dire che la struttura è sbagliata... */
				// nel caso non riesca a processare la richiesta dovrei lanciare un'eccezzione....
				e.printStackTrace();
				
			} catch (NoMoreSolutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} finally {
			lock.unlock();
		}
		
		return lista;
		
	}
	
	/**
	 * Salvo lo stato 
	 * @param name nome da assegnare per un eventuale ritorno 
	 */
	public void savePoint(String name){
		try {
			this.savePoint.put(name, new Theory(this.current_theory.toString()));
		} catch (InvalidTheoryException e) {
			// ... impossibile ...
		}
	}
	
	/**
	 * Recupero lo stato 
	 * @param name nome assegnato allo stato da recuperare
	 * @return vero se riesco a recuperare lo stato
	 */
	public boolean restorePoint(String name){
		if ( this.savePoint.containsKey(name) ) {
			try {
				this.current_theory = new Theory(this.savePoint.get(name).toString());
				return true;
			} catch (InvalidTheoryException e) {
				// .. impossibile ... 
			}
			return false;
		} else return false;
	}
	
	public String toString(){
		return this.current_theory.toString();
	}

	/**
	 * Creo i metadati del database
	 */
	private void createMeta() {
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
}
