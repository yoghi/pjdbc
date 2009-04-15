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

			SolveInfo info = prolog.solve("metabase(TABLE,POSITION,NAME,TYPE).");
			if (info.isSuccess()) {

				while (info.isSuccess()) {

					Term table_name = info.getTerm("TABLE");
					Term field_position = info.getTerm("POSITION");
					Term field_name = info.getTerm("NAME");
					Term field_type = info.getTerm("TYPE");

					if (table_name.isAtom() && field_name.isAtom() && (field_position instanceof Number) && field_type.isAtom()) {

//						ArrayList<TableSpecificField> fields = null;
//
//						if (!this.table.containsKey(table_name.toString())) {
//							fields = new ArrayList<TableSpecificField>();
//							this.table.put(table_name.toString(), fields);
//							Logger.getLogger("it.unibo.lmc.pjdbc").debug("trovati metadati tabella " + table_name.toString());
//						} else {
//							fields = (ArrayList<TableSpecificField>) this.table.get(table_name.toString());
//						}
//
//						TableSpecificField f = new TableSpecificField(field_name.toString());
//						f.setPositionInTable(((Number) field_position).intValue());
//
//						if (field_type.toString().equals("int"))
//							f.setType(java.sql.Types.INTEGER);
//						else if (field_type.toString().equals("string"))
//							f.setType(java.sql.Types.VARCHAR);
//
//						fields.add(f);

					} else {
						//throw new SQLException("Malformed metabase");
						System.out.println("Malformed metabase");
					}

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
