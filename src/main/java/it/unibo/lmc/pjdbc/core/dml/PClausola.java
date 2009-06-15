package it.unibo.lmc.pjdbc.core.dml;

public class PClausola {

	String clausola;
	
	public PClausola(String clausola) {
		this.clausola = clausola;
	}

	public void append(String clausola){
		this.clausola += ","+clausola;
	}
}
