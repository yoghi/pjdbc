package it.unibo.lmc.pjdbc.database.executor;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import it.unibo.lmc.pjdbc.database.command.ICommand;
import it.unibo.lmc.pjdbc.database.command.PResultSet;
import it.unibo.lmc.pjdbc.database.command.ddl.PDrop;
import it.unibo.lmc.pjdbc.database.command.dml.PDelete;
import it.unibo.lmc.pjdbc.database.command.dml.PInsert;
import it.unibo.lmc.pjdbc.database.command.dml.PUpdate;
import it.unibo.lmc.pjdbc.database.command.dml.Pselect;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Drop;
import it.unibo.lmc.pjdbc.parser.dml.imp.DropDB;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

public class ExecuteChild implements ICommand,Runnable {

	private ParsedCommand request;
	private Prolog engine;
	private MSchema metaSchema;
	
	/**
	 * Logger 
	 */
	private Logger log = null;

	public ExecuteChild(Prolog pEngine, MSchema schema, ParsedCommand request) {
		
		this.engine = pEngine;
		this.request = request;
		this.metaSchema = schema;
		this.log = Logger.getLogger(ExecuteControl.class);
		
	}

	public PResultSet applyCommand(Select request) throws PSQLException {
		
		Pselect prq = new Pselect(this.metaSchema,request);
		String gen_psql = prq.generatePrologRequest();
		
		log.debug("psql da eseguire: "+gen_psql);

		List<Term[]> rows = new Vector<Term[]>();
		List<TableField> fields = request.getCampiRicerca();	// il campo alias è il campo con cui si presentano nella SELECT (e.di,$1,etc...)
		
		try {
		
			SolveInfo info = this.engine.solve(gen_psql);
			
			while (info.isSuccess()){ 
				
				log.debug("soluzione"+info.getBindingVars().toString());
				
				Term[] row = new Term[fields.size()];
				int i=0;
				
				for (TableField field : fields) {
					
					String fieldName = field.getQualifiedName();
					String varName = prq.sql2prologVar(fieldName);
					Term t = info.getVarValue(varName);
					row[i] = t;
					i++;
				}
				
				rows.add(row);
				
				if (this.engine.hasOpenAlternatives()){ 
					try {
						info=this.engine.solveNext();
					} catch (NoMoreSolutionException e) {
						break;
					} 
				} else { 
					break;
				}
			}
	
		} catch (MalformedGoalException e) {
			throw new PSQLException(e.getLocalizedMessage(), PSQLState.SYSTEM_ERROR);
		} catch (NoSolutionException e) {
			// non ho soluzionio 
		}
		
		return new PResultSet(fields,rows);

	}
	
	public int applyCommand(Insert request) throws PSQLException {

		/**
		 * Assert
		 */

		PInsert prq = new PInsert(this.metaSchema, request);

		try {

			String requestPsql = prq.generatePrologRequest();

			log.debug("psql da eseguire su " + this.metaSchema.getSchemaName()
					+ " : " + requestPsql);

			SolveInfo info = this.engine.solve(requestPsql);

			log.debug(info.toString());

			if (info.isSuccess()) {
				return 1;
			} else {
				log.error("errore");
				throw new PSQLException("", PSQLState.SYSTEM_ERROR);
			}

		} catch (MalformedGoalException e) {

		}

		return 0;
	}

	public int applyCommand(Update request) throws PSQLException {

		/**
		 * Retract => select delle righe che matchano con la where e loro
		 * rimozione + Assert => delle righe con la modifica
		 */

		PUpdate updateReq = new PUpdate(this.metaSchema, request);

		try {

			String requestPsql = updateReq.generatePrologRequest();

			log.debug("psql da eseguire su " + this.metaSchema.getSchemaName()
					+ " : " + requestPsql);

			SolveInfo info = this.engine.solve(requestPsql);

			log.debug(info.toString());

			int n = 0;
			while (info.isSuccess()) {
				n++;

				if (this.engine.hasOpenAlternatives()) {
					try {
						info = this.engine.solveNext();
					} catch (NoMoreSolutionException e) {
						break;
					}
				} else {
					break;
				}
			}

			return n;

		} catch (MalformedGoalException e) {

		}

		return 0;
	}

	public int applyCommand(Delete request) throws PSQLException {

		/**
		 * Retract => select delle righe che matchano con la where e loro
		 * rimozione
		 */

		PDelete deleteReq = new PDelete(this.metaSchema, request);

		try {

			String requestPsql = deleteReq.generatePrologRequest();

			log.debug("psql da eseguire su " + this.metaSchema.getSchemaName()
					+ " : " + requestPsql);

			SolveInfo info = this.engine.solve(requestPsql);

			log.debug(info.toString());

			int n = 0;
			while (info.isSuccess()) {
				n++;

				if (this.engine.hasOpenAlternatives()) {
					try {
						info = this.engine.solveNext();
					} catch (NoMoreSolutionException e) {
						break;
					}
				} else {
					break;
				}
			}

			return n;

		} catch (MalformedGoalException e) {

		}

		return 0;
	}

	public int applyCommand(Drop request) throws PSQLException {

		/**
		 * Retract
		 */

		PDrop dropReq = new PDrop(this.metaSchema, request);

		try {

			String requestPsql = dropReq.generatePrologRequest();

			log.debug("psql da eseguire su " + this.metaSchema.getSchemaName()
					+ " : " + requestPsql);

			SolveInfo info = this.engine.solve(requestPsql);

			log.debug(info.toString());
			int n = 0;
			while (info.isSuccess()) {
				n++;

				if (this.engine.hasOpenAlternatives()) {
					try {
						info = this.engine.solveNext();
					} catch (NoMoreSolutionException e) {
						break;
					}
				} else {
					break;
				}
			}

			return n;

		} catch (MalformedGoalException e) {

		}

		return 0;
	}

	public void applyCommand(DropDB request) throws PSQLException {
		throw new PSQLException("non implemented yet",
				PSQLState.NOT_IMPLEMENTED);
	}

	public void run() {
		
		
		// TODO da fare....
		
		
		
	}

}
