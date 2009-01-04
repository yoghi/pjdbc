package it.unibo.lmc.pjdbc.core;

import it.unibo.lmc.pjdbc.core.request.IRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

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
public class PrologSingleDB {
	
	ReentrantLock lock = new ReentrantLock();
	
	String schema;
	
	Theory current_theory;
	/** @todo: dovrei poter salvare gli stati su file e non solo su memoria */
	HashMap<String, Theory> savePoint = new HashMap<String, Theory>();
	
	/**
	 * Costruttore PrologDB
	 * @param th teoria prolog da usare come base del DB.
	 */
	public PrologSingleDB(String schemaName,Theory th){
		this.schema = schemaName;
		this.current_theory = th;
		this.createMeta();
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
				
				engine.getTheory();
				
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
