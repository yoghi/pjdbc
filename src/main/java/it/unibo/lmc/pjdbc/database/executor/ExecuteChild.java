package it.unibo.lmc.pjdbc.database.executor;

import it.unibo.lmc.pjdbc.database.command.PRequest;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import alice.tuprolog.InvalidTermException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;

public class ExecuteChild implements Callable<PResultSet> {

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

	protected PResultSet find(Select request) throws PSQLException {
		
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
	
	protected PResultSet apply(ParsedCommand request) throws PSQLException {

		PRequest prq = null;
		
		if ( request instanceof Insert ){
			prq = new PInsert(this.metaSchema, (Insert)request);
		}
		
		if ( request instanceof Delete ){
			prq = new PDelete(this.metaSchema, (Delete)request);
		}
		
		if ( request instanceof Drop ){
			prq = new PDrop(this.metaSchema, (Drop)request);
		}
		
		if ( request instanceof Update ){
			prq = new PUpdate(this.metaSchema, (Update)request);
		}
		
		int n = 0;

		try {

			String requestPsql = prq.generatePrologRequest();

			log.debug("psql da eseguire su " + this.metaSchema.getSchemaName() + " : " + requestPsql);

			SolveInfo info = this.engine.solve(requestPsql);

			log.debug(info.toString());

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

		} catch (MalformedGoalException e) {
			throw new PSQLException("errore nell'analisi di un goal",PSQLState.SYNTAX_ERROR);
		}

		try {
		
			LinkedList<Term[]> rows = new LinkedList<Term[]>();
			LinkedList<TableField> fields = new LinkedList<TableField>();
			TableField tf = new TableField();
			tf.setAlias("AffectedRow");
			fields.add(tf);
			
			Term[] affectedRows = new Term[1];
			affectedRows[0] = Term.createTerm(""+n);
			rows.add(affectedRows);
			
			PResultSet res = new PResultSet(fields, rows);
			return res;
		
		} catch (InvalidTermException e) {
			throw new PSQLException("errore nella creazione di un term",PSQLState.SYNTAX_ERROR);
		}
		
	}


//	protected void applyCommand(DropDB request) throws PSQLException {
//		throw new PSQLException("non implemented yet",PSQLState.NOT_IMPLEMENTED);
//	}

	public PResultSet call() throws PSQLException {
		
		if ( this.request instanceof Select && !(this.request instanceof Delete) ) {
			return this.find((Select) this.request);
		} else {
			return this.apply(this.request);
		}
		
	}

}
