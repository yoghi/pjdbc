package it.unibo.lmc.pjdbc.core.database;

import it.unibo.lmc.pjdbc.core.meta.MColumn;
import it.unibo.lmc.pjdbc.utils.PSQLException;

import java.util.List;
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
	 * Metainformazioni sui campi
	 */
	private List<MColumn> mFields;
	
	/**
	 * Righe/Solution
	 */
	private List<Term[]> rowData;
	
	
	public PResultSet(List<MColumn> fields, List<Term[]> rows) {
		log = Logger.getLogger(PResultSet.class.toString() + "." + this.code);
	}
	
	
	
	protected Term getValue(int columnIndex) throws PSQLException {
		
		PSolution info = this.rowData.get(this.currentPosition);
		try {
			Term t = info.getVar(columnIndex-1);			
			return t;
		} catch (IndexOutOfBoundsException e) {
			throw new PSQLException("Column " + columnIndex + "not exist", PSQLState.DATA_TYPE_MISMATCH );
		}
		
		// if ( vresult.getTerm() instanceof alice.tuprolog.Number )

	}

	/**
	 * Ottengo il term corrispondente alla colonna selezionata
	 * @param columnLabel nome colonna
	 * @return Term
	 * @throws PSQLException
	 */
	protected Term getValue(String columnLabel) throws PSQLException {

		if (null == columnLabel)
			throw new PSQLException("columLabel cann't nullable ",PSQLState.UNDEFINED_COLUMN);

		PSolution info = this.rowData.get(this.currentPosition);

		MColumn minfo = this.mfields.get(columnLabel);
		
		..
		
//		columnLabel = ((Pselect)this.pRequest).alias2nameVar(columnLabel);
//
//		if (null == columnLabel)
//			throw new PSQLException("Column " + columnLabel + " not exist", PSQLState.UNDEFINED_COLUMN);
//
//		String prologLabel = this.pRequest.sql2prologVar(columnLabel);
//
//		if (null == prologLabel) {
//			log.error("Column " + columnLabel+" non trovata corrispondenza con le variabili prolog usate!!");
//			throw new PSQLException("Column " + columnLabel + " not found!", PSQLState.UNDEFINED_COLUMN);
//		}
//
//		Term value = info.getVar(prologLabel);
//
//		if (null == value)
//			throw new PSQLException("Column " + columnLabel + " not exist", PSQLState.UNDEFINED_COLUMN);
		
		
		return info.getVar(minfo);

	}
	
	
}
