package it.unibo.lmc.pjdbc.database.command;

import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;

public class PClausola {

	private MTable metadati;
	
	private String[] terms;
	
	public PClausola(MTable info) {
		this.metadati = info;
		int numero_colonne = info.numColum();
		this.terms = new String[numero_colonne];
	}
	
	public boolean setTerm(String item,int pos,boolean override) throws ArrayIndexOutOfBoundsException {
		
		if ( pos > this.terms.length | pos < 0 ) throw new ArrayIndexOutOfBoundsException();
		
		if ( null == this.terms[pos] ) {
			this.terms[pos] = item;
			return true;
		} else {
			if ( override ) {
				this.terms[pos] = item;
				return true;
			}
		}
		
		return false;
	}
	
	
	public boolean setTerm(String item,String name,boolean override) throws PSQLException {
		int pos = this.metadati.findField(name);
		return this.setTerm(item, pos, override);
	}
	
	public String toString(){
		StringBuilder build = new StringBuilder();
		build.append(this.metadati.getTableName());
		build.append("(");
		for (int i = 0; i < this.terms.length; i++) {
			if ( null == this.terms[i] ) {
				build.append("_");
			} else {
				build.append(this.terms[i]);
			}
			build.append(",");
		}
		build.reverse();
		build.delete(0, 1);
		build.reverse();
		build.append(")");
		return build.toString();
	}
	
}
