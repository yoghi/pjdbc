package it.unibo.lmc.pjdbc.core.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Var;

public class PSolution {

	private List<Var> bindings = new ArrayList<Var>();

	public PSolution(SolveInfo info) {
		this.analize(info);
	}

	/**
	 * Analizzo la soluzione prolog e memorizzo correttamente le informazioni
	 * @param info
	 */
	@SuppressWarnings("unchecked")
	private void analize(SolveInfo info) {
		
		try {
			
			List<Var> tempVar = info.getBindingVars();
			
			Iterator<Var> it = tempVar.iterator();
			while(it.hasNext()) {
				
				Var v = it.next();
				if ( v!=null && !v.isAnonymous() && v.isBound() && (!(v.getTerm() instanceof Var) || ( !( (Var)( v.getTerm()) ).getName().startsWith("_") ) ) ) {
					this.bindings.add(v);
				}
				
			}
			
		} catch (NoSolutionException e) {
			//log.debug("no solution");
		}
		
	}

	/**
	 * Restituisce il valore della variabile nella specifica posizione 
	 * @param index posizione
	 * @return alice.tuprolog.Term
	 */
	public Term getVar(int index) throws IndexOutOfBoundsException {
		return this.bindings.get(index).getTerm();
	}

	/**
	 * Restituisce il valore della variabile 
	 * @param varName nome variabile
	 * @return alice.tuprolog.Term
	 */
	public Term getVar(String varName){
		return this.getVarValue(varName);
	}
	
	/**
	 * Gets the value of a variable in the substitution. Returns <code>null</code>
	 * if the variable does not appear in the substitution.
	 */
	private Term getVarValue(String varName) {
		Iterator<Var> it = bindings.iterator();
		while (it.hasNext()) {
			Var v = it.next();
			if (v!=null && v.getName().equals(varName)) {
				return v.getTerm();
			}
		}
		return null;
	}
	
}
