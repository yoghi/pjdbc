package it.unibo.lmc.pjdbc.core.dml;

import java.util.List;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

public class Pselect {

	private String psql;
	
	public Pselect(Select sql){
		
		//sql.getSchema();
		
		List<TableField> cr = sql.getCampiRicerca();
		List<Table> tb = sql.getFromTable();
		
		// CASO A - 1 tabella 
		
		if ( tb.size() == 1 ) {
			
			psql = tb.get(0).getName()+"(";
			
			String campi = "";
			for (TableField f : cr) {
				
				if ( f.getColumnName().startsWith("$") ) {
					
					String var = "X"+f.getColumnName().substring(1);
					campi += var+",";
					
				} else {
					
					campi += f.getColumnName()+",";
				}
				
			}
			
			psql += campi.substring(0, campi.length()-1)+").";
		}
		
		// CASO B - piu tabelle
		// .-- non implementato --
		
		System.out.println(psql);
		
	}

	public PrologResultSet execute(Theory current_theory) throws InvalidTheoryException, MalformedGoalException, NoSolutionException, UnknownVarException, NoMoreSolutionException {
		
		Prolog p = new Prolog();
		
		p.setTheory(current_theory);
		
		SolveInfo info = p.solve(this.psql);
		
		while (info.isSuccess()){ 
			
			System.out.println( info.toString() );
			
			if (p.hasOpenAlternatives()){ 
				info=p.solveNext(); 
			} else { 
				break;
			}
		}

		return null;
	}
	
	
	
}
