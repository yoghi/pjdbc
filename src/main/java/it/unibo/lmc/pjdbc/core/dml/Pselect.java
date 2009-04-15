package it.unibo.lmc.pjdbc.core.dml;

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
	
	
	public Pselect(Select sql){
		
		//sql.getSchema();
		
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");
		
		List<TableField> cr = sql.getCampiRicerca();
		List<Table> tb = sql.getFromTable();
		
		// CASO A - 1 tabella 
		
		campiRicerca = new String[cr.size()];
		
		if ( tb.size() == 1 ) {
			
			psql = tb.get(0).getName()+"(";
			
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
		
		this.generatePsql();
		
		// CASO B - piu tabelle
		// .-- non implementato --
		
		//System.out.println("PSQL: "+psql);
		
	}
	
	private void generatePsql(){
		
		StringBuffer buff = new StringBuffer();
		for ( int i = 0; i < this.campiRicerca.length; i++ ){
			buff.append(this.campiRicerca[i]);
			buff.append(",");
		}
		
		String campi = buff.toString();
		psql += campi.substring(0, campi.length()-1)+").";
		
		log.info("Psql generato: "+psql);
	}

	public PrologResultSet execute(Theory current_theory) throws InvalidTheoryException, MalformedGoalException, NoSolutionException, UnknownVarException {
		
		List<SolveInfo> rows = new ArrayList<SolveInfo>();
		
		Prolog p = new Prolog();
		
		p.setTheory(current_theory);
		
		SolveInfo info = p.solve(this.psql);
		
		while (info.isSuccess()){ 
			
			List<Var> vars = info.getBindingVars();
			for (Var var : vars) {
				System.out.println(var.getName()+" => "+var.getTerm());
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
