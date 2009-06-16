package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.sql.SQLException;
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
	
//	/**
//	 * Clausole PSQL
//	 * 	tabella1 => campo1,campo2,campo3
//	 *  tabella2 => campo1
//	 *  clausola 1 => null 
//	 */
//	private HashMap<String,String[]> clausolePsql; 
	
	private PRequest requestPsql;
	
	/**
	 * Tabella primaria
	 */
	private String primaryTable;
	
	/**
	 * Costruttore
	 * @param metaSchema meta informazioni sul database su cui si dovrà agire 
	 */
	public Pselect(MSchema metaSchema) {
		this.mschema = metaSchema;
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");
		log.debug("valuto lo schema "+metaSchema.toString());
	}
	
	/**
	 * Costruttore interno per la duplicazione
	 * @param sqlS
	 * @param metaSchema
	 */

	private Pselect(Select sqlS, MSchema metaSchema) {
		this.mschema = metaSchema;
		this.sql = sqlS;
		log = Logger.getLogger("it.unibo.lmc.pjdbc.core.dml");
		log.debug("valuto lo schema "+metaSchema.toString());
	}


	/**
	 * Clone di questa istanza
	 * @return un clone dell'istanza corrente
	 * @throws SQLException 
	 */
	public Pselect duplicate() throws SQLException{
		Pselect p = new Pselect(this.sql,this.mschema);
		p.setAliasTable(this.aliasTable);
		p.setAliasVariable(this.aliasVariable);
		return p;
	}

	/**
	 * Valuto la richiesta SELECT  
	 * @param request richiesta generata con il parsing dell'sql
	 * @throws SQLException 
	 */
	public void evalSql(Select request) throws SQLException {

		this.sql = request;

		/** 0. alias table => Se la tabella non esiste nei metadati?? */
		
		List<Table> tb = this.sql.getFromTable();
		
		this.primaryTable = tb.get(0).getName();
		
		this.aliasTable = new HashMap<String, String>();
		for (int i = 0; i < tb.size(); i++) {
			if ( tb.get(i).getAlias() != null ) this.aliasTable.put(tb.get(i).getAlias(), tb.get(i).getName());	
			else this.aliasTable.put(tb.get(i).getName(), tb.get(i).getName());	// se non c'è alias metto il nome della tabella
		}
		
		// genera clausole primarie (info a partire da FROM e dai campi di SELECT
		this.analisiClausolePrimarie();
		
		// controllo clausole WHERE
		this.analisiClausoleSecondarie();
		
	}
	
	private void analisiClausoleSecondarie() {
		
		Expression whereExp = this.sql.getWhereClausole();
		
		//whereExp.eval(this.requestPsql);
		
		// NON LE COSIDERO PER ORA!!!!
		
	}

	private void analisiClausolePrimarie() throws SQLException{
		
		this.aliasVariable = new HashMap<String, String>();
		
		this.requestPsql = new PRequest();
		this.requestPsql.setSchemaInfo(this.mschema);
		this.requestPsql.setVarInfo(this.aliasVariable); 
		
		List<TableField> cr = this.sql.getCampiRicerca();
		
		/** 1. mi memorizzo le colonne da cercare suddivise per tabella */
		HashMap<String, TableField[]> selectT = new HashMap<String, TableField[]>();
		
		String tname;
		TableField[] c = null;
		
		for (TableField tf : cr) {
			
			if ( tf.getTableName() == null ) {
				tname = this.nameFromAlias(this.primaryTable);		// lo associo alla prima tabella dopo FROM
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
			
		}	//for
		
		boolean first = true;
		for (String tableName : selectT.keySet()) {
			
			TableField[] field = selectT.get(tableName);
			
			StringBuilder str = new StringBuilder();
			str.append(tableName);
			str.append('(');
			for(int i=0; i < field.length; i++){
				if ( field[i] == null ) str.append('_');
				else {
					String psql_var = null;
					if ( field[i].getColumnName().startsWith("$") ){
						String num = field[i].getColumnName().substring(1);
						psql_var = tableName.toUpperCase()+num;
					} else {
						//il campo esiste l'ho controllato prima
						psql_var = field[i].getColumnName().toUpperCase();
					}
					str.append(psql_var);
					this.aliasVariable.put( psql_var , field[i].getColumnName() );
				}
				str.append(',');
			}
			str.replace(str.length()-1, str.length(), ")");
			this.requestPsql.AND(str.toString());

		}
		
	}

	public PRequest generatePsql() {
		return this.requestPsql;
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
	 * Converto l'alias di una tabella con il nome per esteso della tabella
	 * @param aliasName l'alias
	 * @return il nome della tabella per esteso
	 */
	private String nameFromAlias(String aliasName) {
		String a = this.aliasTable.get(aliasName);
		if ( null == a) return aliasName;
		return a;
	}
	
}
