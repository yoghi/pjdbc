package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.SQLException;
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
import alice.tuprolog.Var;

public class Pselect {

	private String psql;

	private Logger log;

	private Select sl;
	
	/**
	 * Alias, Nome Tabella
	 */
	private String[][] aliasTable;
	
	/**
	 * Alias Nome Varibile PSQL,Nome Variabile SQL
	 */
	private HashMap<String, String> aliasVariable;
	
	
	public Pselect(Select sql){	
		sl = sql;
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");		
	}
	
	private void generatePsql(MSchema mschema) throws SQLException{
		
		List<TableField> cr = this.sl.getCampiRicerca();
		List<Table> tb = this.sl.getFromTable();
		
		// 0. alias table => Se la tabella non esiste nei metadati??
		
		this.aliasTable = new String[tb.size()][2];
		for (int i = 0; i < tb.size(); i++) {
			if ( tb.get(i).getAlias() != null ) this.aliasTable[i][0] = tb.get(i).getAlias();	// se non c'è alias metto il nome della tabella
			else this.aliasTable[i][0] = tb.get(i).getName();
			this.aliasTable[i][1] = tb.get(i).getName();
		}
		
		// 1. mi memorizzo le colonne da cercare suddivise per tabella
		HashMap<String, TableField[]> selectT = new HashMap<String, TableField[]>();
		
		String tname;
		TableField[] c = null;
		
		for (TableField tf : cr) {
			
			if ( tf.getTableName() == null ) {
				tname = this.nameFromAlias(tb.get(0).getName());		// lo associo alla prima tabella dopo FROM
			} else {
				tname = this.nameFromAlias(tf.getTableName());
			}
			
			MTable f = mschema.getMetaTableInfo(tname);
			
			if ( f == null ) throw new SQLException("Table "+tname+" not exist in this schema.","SQLSTATE");
			
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
		
		// 2. sistemo secondo la clausola WHERE i campi di richiesta
		
		this.aliasVariable = new HashMap<String, String>();
		Expression whereExp = this.sl.getWhereClausole();
		
		// uso il fatto di avere gia i campi divisi per tabelle come struttura della richiesta e poi vi aggiungo in caso le clausole del where aggiuntive 
		// e manipolo le variaibli assegnando correttamente gli alias poi per la risposta! 
		// employee(X0,X1,X2),X1<29. e non X1<29,employee(X0,X1,X2) quindi le aggiunte vanno messe come parti sucessive!!! 
		// quindi non è piu un semplice array di tabelle ma deve essere qualcosa di piu... o meglio si deve aggiungere una strauttura aggiuntiva.. da usare 
		// quando converto in psql dopo la conversione delle tabelle... tipo "coldVar"
		
		if ( null != whereExp ) {
			
			String[] clausole = whereExp.eval(selectT,mschema,this.aliasVariable);

			System.out.println(whereExp.toString());
			
			System.out.println(whereExp.numClausole());
		}
		
		// 3. converto in psql
		
		StringBuffer tmp_select = new StringBuffer();
		
		for (String tableName : selectT.keySet()) {
			
			TableField[] ftb = selectT.get(tableName);
			
			tmp_select.append(tableName);
			tmp_select.append("(");
			
			for (int i = 0; i < ftb.length; i++) {
                
				if ( ftb[i] == null ){
                	tmp_select.append("_");
				} else {
                
	                if ( ftb[i].getColumnName().startsWith("$") ) {                         // a. converto $N => XN
	                	
	                	String t = tableName.toUpperCase()+ftb[i].getColumnName().substring(1);
	                	tmp_select.append(t); 
	                	this.aliasVariable.put(t,ftb[i].getColumnName());
	                	
	                	
	                } else {
	                	tmp_select.append(ftb[i].getColumnName());
	                	this.aliasVariable.put(ftb[i].getColumnName(), ftb[i].getColumnName());
	                }
				}
				
				if ( i < ftb.length - 1  ) tmp_select.append(",");
			
			}
			
			tmp_select.append(")");
			tmp_select.append(",");
        
		}

		tmp_select.replace(tmp_select.length()-1, tmp_select.length(), ".");
		
		this.psql = tmp_select.toString();
		
		log.info("Psql generato: "+this.psql);
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
	 * @return un PrologResultSet  
	 * @throws SQLException 
	 */
	public PrologResultSet execute(Theory current_theory, MSchema schema) throws SQLException {
		
		try {
		
			this.generatePsql(schema);
			
			List<SolveInfo> rows = new ArrayList<SolveInfo>();
			
			Prolog p = new Prolog();
			
			p.setTheory(current_theory);
			
			SolveInfo info = p.solve(this.psql);
			
			while (info.isSuccess()){ 
				
				List<Var> vars = info.getBindingVars();
				for (Var var : vars) {
//					log.debug(var.getName()+" => "+var.getTerm());
				}
				
//				log.debug("--");
				
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
	
		} catch (InvalidTheoryException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		} catch (MalformedGoalException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		} catch (NoSolutionException e) {
			throw new SQLException(e.getLocalizedMessage(),"SQLSTATE");
		}
	}
	
	
	
}
