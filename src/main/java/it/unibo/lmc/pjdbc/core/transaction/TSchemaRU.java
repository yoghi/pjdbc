package it.unibo.lmc.pjdbc.core.transaction;

import it.unibo.lmc.pjdbc.core.PSchema;
import it.unibo.lmc.pjdbc.core.command.PResultSet;
import it.unibo.lmc.pjdbc.core.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

import java.util.UUID;

/**
 * 
 * Transaction Schema : TRANSACTION_READ_UNCOMMITTED
 * 
 * Leggo e scrivo direttamente sullo schema, non ho meccanismi di sicurezza 
 * 
 * Ho Dirty Reads, Nonrepeatable Reads, Phantom Reads
 *
 */
public class TSchemaRU extends TSchema {

	public TSchemaRU(PSchema schema) {
		super(schema);
	}

	@Override
	public PResultSet applyCommand(Select request) throws PSQLException {
		this.log.add(request);
		return this.realSchema.applyCommand(request);
	}

	@Override
	public int applyCommand(Insert request) throws PSQLException {
		this.log.add(request);
		return this.realSchema.applyCommand(request);
	}

	@Override
	public int applyCommand(Update request) throws PSQLException {
		this.log.add(request);
		return this.realSchema.applyCommand(request);
	}

	@Override
	public int applyCommand(Delete request) throws PSQLException {
		this.log.add(request);
		return this.realSchema.applyCommand(request);
	}

	@Override
	public void rollback() {
		
		//TODO : itero sull'array di elementi e eseguo l'operazione contraria
		ParsedCommand[] tmp = null;
		tmp = this.log.toArray(tmp);
		//this.realSchema.

		this.currentTransactionID = UUID.randomUUID();
	}

	@Override
	public void commit() throws PSQLException {
		this.log.clear();
		this.realSchema.commit();
		this.currentTransactionID = UUID.randomUUID();
	}

	
}
