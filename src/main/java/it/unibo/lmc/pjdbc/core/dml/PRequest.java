package it.unibo.lmc.pjdbc.core.dml;

import java.util.ArrayList;
import java.util.List;

import it.unibo.lmc.pjdbc.parser.dml.expression.ICondition;

public class PRequest {

	private String schemaName;
	
	/**
	 * Clausola di partenza
	 */
	private PClausola currentClausola;
	
	/**
	 * Nuove clausole 
	 */
	private List<PClausola> clausole;
	
	/**
	 * Posizione corrente (cambia quando arriva un OR)
	 */
	private int currentPos;
	
	private ICondition condition;

	public PRequest( String clausola  ) {
		this.currentClausola = new PClausola(clausola);
		this.clausole = new ArrayList<PClausola>();
	}
	
	public PRequest() {
		this.clausole = new ArrayList<PClausola>();
	}
	
	
	public void AND(String clausola) {
		// aggiungo tanto è già una string quindi è usabile
	}
	
	public void AND(PRequest prequest){
		//problema non so cosa c'è dentro...
		
	}

	public PRequest duplicate() {
		//TODO: da implementare 
		return null;
	}

	public void OR(String clausola) {
		
	}

	public String getPsql() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
