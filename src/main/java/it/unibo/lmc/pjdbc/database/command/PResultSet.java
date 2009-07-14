package it.unibo.lmc.pjdbc.database.command;

import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import alice.tuprolog.Term;

public class PResultSet {

	/**
	 * Identificativo del risultato
	 */
	private UUID code = UUID.randomUUID();
	
	/**
	 * Log
	 */
	protected Logger log;

	/**
	 * Descrittore Campi
	 */
	protected List<TableField> tableFields;
	
	/**
	 * Righe/Solution
	 */
	protected List<Term[]> rowData;
	
	/**
	 * Cache Var => posizione
	 */
	protected Map<String,Integer> cacheNameVar = new HashMap<String,Integer>();
	
	/**
	 * Mi posiziono prima della prima riga
	 */
	protected int currentPosition = -1;
	
	/**
	 * Costruttore 
	 * @param fields
	 * @param rows
	 * @param schema
	 */
	public PResultSet(List<TableField> fields , List<Term[]> rows) {
		log = Logger.getLogger(PResultSet.class.toString() + "." + this.code);
		this.rowData = rows;
		this.tableFields = fields;
		
		int pos = 1;
		for(TableField field : this.tableFields){
			this.cacheNameVar.put(field.getAlias(), pos);
			pos++;
		}
		
	}


	/**
	 * Restituisco il contenuto della riga nella posizione indicata
	 * @param columnIndex
	 * @return
	 * @throws PSQLException
	 */
	public Term getValue(int columnIndex) throws PSQLException {
		
		Term[] info = this.rowData.get(this.currentPosition);
		try {
			Term t = info[columnIndex-1];
			return t;
		} catch (IndexOutOfBoundsException e) {
			throw new PSQLException("Column " + columnIndex + " not exist", PSQLState.DATA_TYPE_MISMATCH );
		}
		
		// if ( vresult.getTerm() instanceof alice.tuprolog.Number )

	}

	/**
	 * Ottengo il term corrispondente alla colonna selezionata
	 * @param columnLabel nome colonna
	 * @return Term
	 * @throws PSQLException
	 */
	public Term getValue(String columnLabel) throws PSQLException {

		if (null == columnLabel)
			throw new PSQLException("columLabel cann't nullable ",PSQLState.UNDEFINED_COLUMN);

		Integer indexColumn = this.cacheNameVar.get(columnLabel);
		
		if (null == indexColumn)
			throw new PSQLException("columnLabel "+columnLabel+" not found",PSQLState.UNDEFINED_COLUMN);
		
		return this.getValue(indexColumn); 

	}
	
	/**
	 * Sposta il cursore alla riga sucessiva
	 * @return false se ho raggiunto la posizione massima
	 */
	public boolean next(){
		if ( this.currentPosition < this.rowData.size()-1 ) {
			this.currentPosition++;
			return true;
		}
		return false;
	}
	
	public boolean first(){
		this.currentPosition = 0;
		return this.rowData.size() > 0;
	}
	
	public void last(){
		this.currentPosition = this.rowData.size()-1;
	}


	public int getFetchSize() {
		return this.rowData.size();
	}


	/**
	 * Elenco dei campi coinvolti
	 * @return
	 */
	public List<TableField> getFields() {
		return this.tableFields;
	}


	public int findColumn(String columnLabel) throws PSQLException {
		
		Integer pos = this.cacheNameVar.get(columnLabel);
		
		if ( null == pos ) throw new PSQLException("invalid column label ", PSQLState.UNDEFINED_COLUMN);
		return pos;
	}


	public boolean previous() {
		if ( this.currentPosition > 0 ) {
			this.currentPosition--;
			return true;
		}
		return false;
	}
	
}
