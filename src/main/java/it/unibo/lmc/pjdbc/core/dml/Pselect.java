package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class Pselect {

	/**
	 * Logger di sistema
	 */
	private Logger log;

	/**
	 * Query Sql Parsed
	 */
	private Select sql;
	
	/**
	 * Meta Schema
	 */
	private MSchema mschema;
	
	/**
	 * Alias Nome Tabella Breve, Nome Tabella
	 */
	private HashMap<String, String> aliasTable;
	
	/**
	 * Alias Nome Varibile PSQL,Nome Variabile SQL
	 */
	private HashMap<String, String> aliasVariable;
	
	/**
	 * Converto l'alias di una tabella con il nome per esteso della tabella
	 * @param aliasName l'alias
	 * @return il nome della tabella per esteso
	 */
	private String nameFromAlias(String aliasName) {
		String a = this.aliasTable.get(aliasName);
		if ( null == a) return aliasName;
		return a;
	}
	
	/**
	 * Clone di questa istanza
	 * @return un clone dell'istanza corrente
	 */
	public Pselect duplicate(){
		Pselect p = new Pselect(this.sql,this.mschema);
		p.setAliasTable(this.aliasTable);
		p.setAliasVariable(this.aliasVariable);
		return p;
	}

	/**
	 * @param aliasTable the aliasTable to set
	 */
	private void setAliasTable(HashMap<String, String> aliasTable) {
		this.aliasTable = aliasTable;
	}

	/**
	 * @param aliasVariable the aliasVariable to set
	 */
	private void setAliasVariable(HashMap<String, String> aliasVariable) {
		this.aliasVariable = aliasVariable;
	}
	
	/**
	 * Costruttore 
	 * @param request richiesta generata con il parsing dell'sql
	 * @param metaSchema meta informazioni sul database su cui si dovrà agire
	 */
	public Pselect(Select request, MSchema metaSchema) {
		this.sql = request;
		this.mschema = metaSchema;
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");
	}

	/**
	 * Generazione prolog request in base alle informazioni attuali
	 * @return stringa/e contenente la richiesta prolog da eseguire
	 * @throws SQLException
	 */
	public String[] generatePsql() throws SQLException{
		
		ArrayList<String> psql = new ArrayList<String>();
		
		List<TableField> cr = this.sql.getCampiRicerca();
		List<Table> tb = this.sql.getFromTable();
		
		/** 0. alias table => Se la tabella non esiste nei metadati?? */
		
		this.aliasTable = new HashMap<String, String>();
		for (int i = 0; i < tb.size(); i++) {
			if ( tb.get(i).getAlias() != null ) this.aliasTable.put(tb.get(i).getAlias(), tb.get(i).getName());	
			else this.aliasTable.put(tb.get(i).getName(), tb.get(i).getName());	// se non c'è alias metto il nome della tabella
		}
		
		/** 1. mi memorizzo le colonne da cercare suddivise per tabella */
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
		
		/** 2. sistemo secondo la clausola WHERE i campi di richiesta */
		
		this.aliasVariable = new HashMap<String, String>();
		Expression whereExp = this.sql.getWhereClausole();
		
		if ( null != whereExp ) {
			
			//whereExp.eval(this);
			
			// uso il fatto di avere gia i campi divisi per tabelle come struttura della richiesta e poi vi aggiungo in caso le clausole del where aggiuntive 
			// e manipolo le variaibli assegnando correttamente gli alias poi per la risposta! 
			// employee(X0,X1,X2),X1<29. e non X1<29,employee(X0,X1,X2) quindi le aggiunte vanno messe come parti sucessive!!! 
			// quindi non è piu un semplice array di tabelle ma deve essere qualcosa di piu... o meglio si deve aggiungere una strauttura aggiuntiva.. da usare 
			// quando converto in psql dopo la conversione delle tabelle... tipo "coldVar"

			System.out.println(whereExp.toString());
			
			System.out.println(whereExp.numClausole());
		}
		
		/** 3. converto in psql */
		
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
		
		log.info("Psql generato: "+tmp_select.toString());
		
		psql.add(tmp_select.toString());
		
		return psql.toArray(new String[1]);
	}
	
}
