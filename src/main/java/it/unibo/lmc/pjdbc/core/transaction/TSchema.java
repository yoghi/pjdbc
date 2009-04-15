package it.unibo.lmc.pjdbc.core.transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import it.unibo.lmc.pjdbc.core.database.PSchema;
import it.unibo.lmc.pjdbc.core.dml.IDml;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

/**
 * Transaction Schema
 *
 */
public abstract class TSchema implements IDml {

	protected UUID currentTransactionID;
	
	/**
	 * Array dei comandi eseguiti durante la transizione corrente
	 */
	protected ArrayList<ParsedCommand> log = new ArrayList<ParsedCommand>();
	
	//TODO : da notare bene che i log devono essere persistenti ... va fatta una classe che gestisca la cosa meglio...
	
	/**
	 * Schema 
	 */
	protected PSchema realSchema;
	
	public TSchema(PSchema schema){
		this.realSchema = schema;
		this.currentTransactionID = UUID.randomUUID();
	}
	
	/*
	 * TRANSAZIONI
	 */
	
	/**
	 * Devo ripristinare una vecchia situazione, quindi rieseguo al contrario le operazioni effettuate
	 * in maniera però "atomica"
	 */
	public abstract void rollback();
	
	/**
	 * Committo la situzione attuale sul database.
	 */
	public abstract void commit();
	
	/*
	 * A.P.I DML 
	 */
	public abstract PrologResultSet applyCommand(Select request) throws SQLException;
	public abstract int applyCommand(Insert request) throws SQLException;
	public abstract int applyCommand(Update request) throws SQLException;
	public abstract int applyCommand(Delete request) throws SQLException;
	
	
}
