package it.unibo.lmc.pjdbc.utils;

import java.io.PrintStream;
import java.util.ArrayList;

public class CacheTheoryString {

	/**
	 * Cache 
	 */
	ArrayList<String> cache = new ArrayList<String>();
	
	/**
	 * Quanti elementi validi ci sono in cache
	 */
	private int valid;
	
	
	public CacheTheoryString() {}
	
	/**
	 * Aggiungo una stringa alla cache
	 * @param o la stringa in questione
	 * @return la posizione in cui la salvo
	 */
	public int add(String o){
		this.cache.add(o);
		this.valid++;
		return this.cache.size()-1;
	}
	
	/**
	 * Rimuovo una stringa specifica dalla cache
	 * @param line posizione della stringa nella cache
	 * @throws IndexOutOfBoundsException
	 */
	public void remove(int line) throws IndexOutOfBoundsException {
		this.change(line, null);
		this.valid--;
	}
	
	/**
	 * Richiedo la stringa di posizione <i>line</i> nella cache
	 * @param line dove è posizionata la stringa
	 * @return la stringa cercata , <i>null</i> se la stringa è stata rimossa
	 * @throws IndexOutOfBoundsException la posizione richiesta non esista
	 */
	public String get(int line) throws IndexOutOfBoundsException {
		return this.cache.get(line);
	}
	
	/**
	 * Cambio il valore di una stringa nella cache
	 * @param line posizione nella cache
	 * @param newVal nuovo valore
	 * @return vecchio valore
	 */
	public String change(int line,String newVal) throws IndexOutOfBoundsException {
		String oldVal = this.get(line);
		this.cache.set(line, newVal);
		return oldVal;
	}
	
	/**
	 * Restituisco il contenuto della cache sottoforma di unica stringa
	 */
	public String toString(){
		StringBuffer buff = new StringBuffer();
		for (String o : this.cache) {
			buff.append(o+" ");
		}
		return buff.toString();
	}

	/**
	 * Dimensione della cache attuale
	 * @return intero
	 */
	public int size() {
		return this.cache.size();
	}
	
	/**
	 * Restituisce il numero di elementi validi in cache
	 * @return intero
	 */
	public int numValidString(){
		return valid;
	}
	
	/**
	 * Stampo la cache
	 * @param out lo stream su cui visualizzarla
	 */
	public void show(PrintStream out){
		for (String o : this.cache) {
			out.println(o);
		}
	}
	
}
