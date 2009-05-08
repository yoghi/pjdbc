package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.ArrayList;
import java.util.HashMap;
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

	private Logger log;

	private Select sl;
	
	/**
	 * Alias, Nome Tabella
	 */
	private String[][] aliasTable;
	
	
	public Pselect(Select sql){
		
		sl = sql;
		
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");
				
	}
	
	private void generatePsql(MSchema mschema){
		
		List<TableField> cr = this.sl.getCampiRicerca();
		List<Table> tb = this.sl.getFromTable();
		
		// 0. alias table
		
		this.aliasTable = new String[tb.size()][2];
		for (int i = 0; i < tb.size(); i++) {
			if ( tb.get(i).getAlias() != null ) this.aliasTable[i][0] = tb.get(i).getAlias();	// se non c'è alias metto il nome della tabella
			else this.aliasTable[i][0] = tb.get(i).getName();
			this.aliasTable[i][1] = tb.get(i).getName();
		}
		
		// 1. assegno alle colonne senza tabella  , la prima tabella usata nella select e mi memorizzo le colonne da cercare suddivise per tabella
		
		HashMap<String, TableField[]> selectT = new HashMap<String, TableField[]>();
		
		String tname;
		TableField[] c;
		
		for (TableField tf : cr) {
			
			if ( tf.getTableName() == null ) {
				tname = this.nameFromAlias(tb.get(0).getName());
			} else {
				tname = tf.getTableName();
			}
			
			MTable f = mschema.getMetaTableInfo(tname);
			
			if ( selectT.containsKey(tname) ){
				c = selectT.get(tname);
			} else {
				
				c = new TableField[f.numColum()];
				selectT.put(tname, c);
			}
			
			int pos = -1;
			if ( tf.getColumnName().startsWith("$")  ) {
				pos = Integer.parseInt(tf.getColumnName().substring(1));
			} else {
				pos = f.containsField(tf.getColumnName());
			}
			
			if ( pos >= 0 ){
				if ( pos < c.length ){
					c[pos] = tf;
				} else {
					log.warn("richiesto campo "+tf.getColumnName()+" non valido sulla tabella "+tname);
				}
			} else {
				log.warn("richiesto campo "+tf.getColumnName()+" non valido sulla tabella "+tname);
			}
			
		}
		
		// 2. comincio a creare le singole stringhe di richiesta
		
		String sql_select = "";	//TODO converti in BufferedString
		MTable mTable;
		
		for (Table tselect : tb) {
			
			mTable = mschema.getMetaTableInfo(tselect.getName());
			
			TableField[] ftb = selectT.get(tselect.getName());
			
			sql_select = tselect.getName()+"(";
			
			for (int i = 0; i < ftb.length; i++) {
				if ( ftb[i] == null ){
					
					//devo correggere ... $0...$X sono i possibili campi di richiesta e poi devo mettere quelli necessari per completare 
					//la tabella
					sql_select += "_,";
					
				} else {
					
					if ( ftb[i].getColumnName().startsWith("$") ) {				// a. converto $N => XN
						sql_select += "X"+ftb[i].getColumnName().substring(1)+",";
					} else {
						int pos = mTable.containsField(ftb[i].getColumnName()); // b. converto nome campo => XN
						if ( pos >= 0){
							sql_select += "X"+pos+",";
						}
					}
					
				}
			}
			
			this.psql = sql_select.substring(0, sql_select.length()-1)+"),";
			
		}

		this.psql = this.psql.substring(0,this.psql.length()-1)+".";
		
		// 3. sistemo secondo la clausola WHERE i campi di richiesta 
		
		

//		MTable metaTable = schema.getMetaTableInfo(this.campiRicerca[0][0]);
//		
//		//metaTable.numColum(); ho queste colonne
//		
//		//psql = this.tables[0] +"(";
//		
//		StringBuffer buff = new StringBuffer();
//		for ( int i = 0; i < this.campiRicerca.length; i++ ){
//			buff.append(this.campiRicerca[i]);
//			buff.append(",");
//		}
//		
//		String campi = buff.toString();
//		psql += campi.substring(0, campi.length()-1)+").";
		
		log.info("Psql generato: "+psql);
	}

	/**
	 * Converto l'alias di una tabella con il nome per esteso della tabella
	 * @param aliasName l'alias
	 * @return il nome della tabella per esteso
	 */
	private String nameFromAlias(String aliasName) {
		for (int i = 0; i < aliasTable.length; i++) {
			if ( aliasTable[i][0].equalsIgnoreCase(aliasName) ) return aliasTable[i][1];
		}
		return aliasName;
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
