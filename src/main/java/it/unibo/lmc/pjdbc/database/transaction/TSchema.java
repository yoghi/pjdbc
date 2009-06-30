package it.unibo.lmc.pjdbc.database.transaction;

import it.unibo.lmc.pjdbc.database.PSchema;
import it.unibo.lmc.pjdbc.database.command.IDml;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

import java.util.ArrayList;
import java.util.UUID;

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
	public abstract void commit() throws PSQLException;
	
	/*
	 * A.P.I DML 
	 */
	public abstract PResultSet applyCommand(Select request) throws PSQLException;
	public abstract int applyCommand(Insert request) throws PSQLException;
	public abstract int applyCommand(Update request) throws PSQLException;
	public abstract int applyCommand(Delete request) throws PSQLException;

	public void close() {
		this.realSchema.close();
	}
	
	
}
