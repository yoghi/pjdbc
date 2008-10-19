package it.unibo.lmc.pjdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class ParsedRequest {
	
	/**
	 * Struttura tabella/campi
	 */
	private LinkedList<ArrayList> table = new LinkedList<ArrayList>();
	
	/**
	 * Nomi eventuali delle tabelle
	 */
	private LinkedList<String> table_name = new LinkedList<String>();
	
	/**
	 * Tabella corrente a cui associare i campi
	 */
	private int currentTableId = -1;
	
	/**
	 * Setto i nomi dei campi da selezionare
	 * @param tname, nome tabella
	 * @param fname, nome campo
	 */
	public void setField(String tname, String fname){
		
		if ( this.table_name.contains(tname) ){
			
			//aggiungo campo ad una tabella gia inserita
			this.currentTableId = this.table_name.indexOf(tname);
			ArrayList<MetaField> f = this.table.get(this.currentTableId);
			f.add(new MetaField(fname));
			
		} else {
			
			//aggiungo campo ad una tabella di nuova conoscenza
			this.table_name.add(tname);
			this.currentTableId = this.table_name.size()-1;
			ArrayList<MetaField> f = new ArrayList<MetaField>();
			f.add(new MetaField(fname));
			this.table.add(f);
			
		}
		
	}
	
	/**
	 * Setto il nome del campo da selezionare
	 * @param fname, nome campo
	 */
	public void setField(String fname){
		
		// controllo se ho già un field array disponibile
		if ( this.currentTableId > -1 ){
			
			//tabella corrente inizializzata
			ArrayList<MetaField> f = this.table.get(this.currentTableId);
			f.add(new MetaField(fname));
			
			Logger.getLogger("it.unibo.lmc.pjdbc.core").debug("campo => "+fname);
			
		} else {
		
			//tabella corrente non inizializzata
			ArrayList<MetaField> f = new ArrayList<MetaField>();
			f.add(new MetaField(fname));
			this.table.add(f);
			
			this.currentTableId = this.table.size()-1;
			Logger.getLogger("it.unibo.lmc.pjdbc.core").debug("campo => "+fname);
		}
		
	}
	
	/**
	 * Setto i nomi delle tabelle in gioco
	 * @param tname , nome della tabella
	 */
	public void setTable(String tname){
		if ( !this.table_name.contains(tname) ) {
			Logger.getLogger("it.unibo.lmc.pjdbc.core").debug("tabella => "+tname);
			this.table_name.add(tname);
		}
	}
	
	/**
	 * Identificativo dell'ultima tabella utilizzata
	 * @return id
	 */
	public int getCurrentTable(){
		return this.currentTableId;
	}
	
	/**
	 * Numero di tabelle coinvolte
	 * @return numero delle tabelle
	 */
	public int getNumTable(){
		return this.table_name.size();
	}

	/**
	 * Restituisce il nome della tabella di posizione i
	 * @param i
	 * @return
	 */
	public String getTableNameByPosition(int i) throws IndexOutOfBoundsException {
		return this.table_name.get(i);
	}

	/**
	 * Restituisce i campi da selezionare della tabella di indice i
	 * @param i indice della tabella
	 * @return 
	 */
	public ArrayList<MetaField> getTableField(int i) {
		return this.table.get(i);
	}
	
}
