package it.unibo.lmc.pjdbc.core.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;

public class MSchema {

	HashMap<String, MTable> tables = new HashMap<String, MTable>();
	
	public MSchema() {}
	
	public void loadFromTheory(Theory th) {
		
		
		try {

			Prolog prolog = new Prolog();
			prolog.setTheory(th);

			ArrayList<SolveInfo> soluzioni = new ArrayList<SolveInfo>();
			
			SolveInfo info = prolog.solve("metabase(TABLE,POSITION,NAME,TYPE).");
			if (info.isSuccess()) {

				while (info.isSuccess()) {

					soluzioni.add(info);
					
					if ( prolog.hasOpenAlternatives() ){
						try {
							info = prolog.solveNext();
						} catch (NoMoreSolutionException e) {
							break;
						}
					} else {
						break;
					}
					
				}
				
				// ora controllo le soluzioni
				for (SolveInfo solveInfo : soluzioni) {
					
					Term table_name = solveInfo.getTerm("TABLE");
					Term field_position = solveInfo.getTerm("POSITION");
					Term field_name = solveInfo.getTerm("NAME");
					Term field_type = solveInfo.getTerm("TYPE");

					if (table_name.isAtom() && field_name.isAtom() && (field_position instanceof Number) && field_type.isAtom()) {

						MTable t;
						if ( this.tables.containsKey(table_name.toString()) ) {
							t = this.tables.get(table_name.toString());
						} else {
							t  = new MTable(1);
							this.tables.put(table_name.toString(), t);
						}
						
						t.addField( ((Number) field_position).intValue() , field_name.toString(), field_type.toString() ) ;
						

					} else {
						//throw new SQLException("Malformed metabase");
						System.out.println("Malformed metabase");
					}
					
				}
			
			for (String tname : this.tables.keySet()) {
				System.out.println(this.tables.get(tname).toString());
			}	

			} else {

				Iterator i = th.iterator(new Prolog());
				while(i.hasNext()){
					Term t = (Term)i.next();
					if ( t instanceof Struct ){
			        	
			        	Struct s = (Struct)t;
			        	
			        	// rimane il caso "predicato(...):-!"
			        	if ( s.isGround() ){
			        		//NB: X e _ sono due variabili
			        		//System.out.println("Ground (non contiene variabili) "+x.isGround());	        		
			        		int l = s.getArity();
			        		System.out.println(s.getName()+"/"+l);
			        	}
			        }
				}
				
			}

		} catch (MalformedGoalException e) {
			e.printStackTrace();
		} catch (NoSolutionException e) {
			e.printStackTrace();
		} catch (UnknownVarException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (InvalidTheoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
