package it.unibo.lmc.pjdbc.database.transaction;

import it.unibo.lmc.pjdbc.database.PrologDatabase;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.core.PSchema;
import it.unibo.lmc.pjdbc.database.executor.ExecuteChild;
import it.unibo.lmc.pjdbc.database.executor.ExecuteControl;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

import java.util.UUID;
import java.util.concurrent.Future;

import alice.tuprolog.Prolog;

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

	public TSchemaRU(PrologDatabase db, PSchema schema) {
		super(db,schema);
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

	@Override
	public PResultSet applyCommand(ParsedCommand request) throws PSQLException {
		
		try {
			
			this.log.add(request);
			
			MSchema minfo = this.database.getMetaSchema(this.realSchema.getName());
			
			Prolog pEngine = new Prolog();
			
			pEngine.setTheory(this.realSchema.getTheory());
			
			ExecuteControl control = this.database.getExecutor();
			
			ExecuteChild task = new ExecuteChild(pEngine,minfo,request);
			
			Future<PResultSet> res = control.execute(task);
			
			while(res.isDone()){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			this.realSchema.setTheory(pEngine.getTheory());	//lo applico subito!!
			
			return res.get();
			
		} catch (Exception e) {
			throw new PSQLException("Errore durante l'esecuzione di "+request, PSQLState.SYSTEM_ERROR);
		}
	}
	
}

//@Override
//public PResultSet applyCommand(Select request) throws PSQLException {
//	this.log.add(request);
//	return this.realSchema.applyCommand(request);
//}
//
//@Override
//public int applyCommand(Insert request) throws PSQLException {
//	this.log.add(request);
//	return this.realSchema.applyCommand(request);
//}
//
//@Override
//public int applyCommand(Delete request) throws PSQLException {
//	this.log.add(request);
//	return this.realSchema.applyCommand(request);
//}
//
//@Override
//public int applyCommand(Update request) throws PSQLException {
//	this.log.add(request);
//	return this.realSchema.applyCommand(request);
//}
//public void applyCommand(DropDB request) throws PSQLException {
//this.log.add(request);
//this.realSchema.applyCommand(request);
//}
//
//public int applyCommand(Drop request) throws PSQLException {
//this.log.add(request);
//return this.realSchema.applyCommand(request);
//}
