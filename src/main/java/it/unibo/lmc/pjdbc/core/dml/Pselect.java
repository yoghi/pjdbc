package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;
import alice.tuprolog.Var;

public class Pselect {

	private String psql;
	
	private String[] campiRicerca;

	private Logger log;

	private String[] tables;
	
	
	public Pselect(Select sql){
		
		//sql.getSchema();
		
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");
		
		List<TableField> cr = sql.getCampiRicerca();
		List<Table> tb = sql.getFromTable();
		
		// CASO A - 1 tabella 
		
		campiRicerca = new String[cr.size()];

		this.tables = new String[tb.size()];
		
		if ( tb.size() == 1 ) {
			
			this.tables[0] = tb.get(0).getName();
			
			int i = 0;
			
			for (TableField f : cr) {
				
				if ( f.getColumnName().startsWith("$") ) {
					
					campiRicerca[i] = "X"+f.getColumnName().substring(1); 
					
				} else {
					
					campiRicerca[i] = f.getColumnName();
					
				}
				
				i++;
				
			}
			
		}
		
		// CASO B - piu tabelle
		// .-- non implementato --
		
		//System.out.println("PSQL: "+psql);
		
	}
	
	private void generatePsql(MSchema schema){
		
		//devo correggere ... $0...$X sono i possibili campi di richiesta e poi devo mettere quelli necessari per completare 
		//la tabella
		
		MTable metaTable = schema.getMetaTableInfo(this.tables[0]);
		
		//metaTable.numColum(); ho queste colonne
		
		psql = this.tables[0] +"(";
		
		StringBuffer buff = new StringBuffer();
		for ( int i = 0; i < this.campiRicerca.length; i++ ){
			buff.append(this.campiRicerca[i]);
			buff.append(",");
		}
		
		String campi = buff.toString();
		psql += campi.substring(0, campi.length()-1)+").";
		
		log.info("Psql generato: "+psql);
	}

	/**
	 * 
	 * @param current_theory la teoria su cui eseguire la richiesta
	 * @param schema le meta-informazioni sulla teoria passata (quindi di tutte le tabelle presenti)
	 * @return
	 * @throws InvalidTheoryException
	 * @throws MalformedGoalException
	 * @throws NoSolutionException
	 * @throws UnknownVarException
	 */
	public PrologResultSet execute(Theory current_theory, MSchema schema) throws InvalidTheoryException, MalformedGoalException, NoSolutionException, UnknownVarException {
		
		this.generatePsql(schema);
		
		List<SolveInfo> rows = new ArrayList<SolveInfo>();
		
		Prolog p = new Prolog();
		
		p.setTheory(current_theory);
		
		SolveInfo info = p.solve(this.psql);
		
		while (info.isSuccess()){ 
			
			List<Var> vars = info.getBindingVars();
			for (Var var : vars) {
				log.debug(var.getName()+" => "+var.getTerm());
			}
			
			rows.add(info);
			
			if (p.hasOpenAlternatives()){ 
				try {
					info=p.solveNext();
				} catch (NoMoreSolutionException e) {
					break;
				} 
			} else { 
				break;
			}
			
		}

		return null;
	}
	
	
	
}
