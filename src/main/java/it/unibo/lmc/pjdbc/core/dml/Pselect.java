package it.unibo.lmc.pjdbc.core.dml;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import it.unibo.lmc.pjdbc.core.database.PRequest;
import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.core.meta.MTable;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;


public class Pselect extends PRequest {

	private Object primaryTable;

	public Pselect(MSchema ms, Select req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws SQLException {
		
		/** 0. alias table => Se la tabella non esiste nei metadati?? */
		
		List<Table> tb = ((Select)this.mcommand).getFromTable();
		
		String primaryTableName = tb.get(0).getName();
		
		
		for (int i = 0; i < tb.size(); i++) {
			if ( tb.get(i).getAlias() != null ) this.aliasTables.put(tb.get(i).getAlias(), tb.get(i).getName());	
			else this.aliasTables.put(tb.get(i).getName(), tb.get(i).getName());	// se non c'è alias metto il nome della tabella
		}
		
		// genera clausole primarie (info a partire da FROM e dai campi di SELECT
		this.analisiClausolePrimarie();
		
		// controllo clausole WHERE
//		this.analisiClausoleSecondarie();
		
//		return this.requestPsql;
		return "";
		
	}

	private void analisiClausolePrimarie() throws SQLException{
		
		List<TableField> cr = ((Select)this.mcommand).getCampiRicerca();
		
		/** 1. mi memorizzo le colonne da cercare suddivise per tabella , l'ordine delle tabelle non è quello che compare nella select */
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
			
			
			String columnName = tf.getColumnName();
			int pos = -1;
			if ( columnName.startsWith("$")  ) {
				pos = Integer.parseInt(columnName.substring(1));
			} else {
				pos = f.containsField(columnName);
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
		
		for ( Table table : ((Select)this.mcommand).getFromTable() ){
			
			String tableName = table.getName();
			
			TableField[] field = selectT.get(tableName);
			
			StringBuilder str = new StringBuilder();
			str.append(tableName);
			str.append('(');
			for(int i=0; i < field.length; i++){
				if ( field[i] == null ) str.append('_');
				else {
					String psql_var = tableName.toUpperCase()+"_";
					if ( field[i].getColumnName().startsWith("$") ){
						String num = field[i].getColumnName().substring(1);
						psql_var += num;
					} else {
						psql_var += field[i].getColumnName().toUpperCase();
					}
					str.append(psql_var);
					
					String sql_var = ( null != field[i].getAlias() ) ? field[i].getAlias() : tableName.toUpperCase()+"."+field[i].getColumnName(); 
					this.aliasVariable.put( psql_var , sql_var ); //posso avere gli alias anche delle var a livello di select!!!
				}
				str.append(',');
			}
			str.replace(str.length()-1, str.length(), ")");
			this.requestPsql.AND(str.toString());

		}
	}
	
	private void analisiClausoleSecondarie() {
		
		Expression whereExp = ((Select)this.mcommand).getWhereClausole();
		
		//whereExp.eval(this.requestPsql);
		
		// NON LE COSIDERO PER ORA!!!!
		
	}
	
	/**
	 * Converto l'alias di una tabella con il nome per esteso della tabella
	 * @param aliasName l'alias
	 * @return il nome della tabella per esteso
	 */
	private String nameFromAlias(String aliasName) {
		String a = this.aliasTables.get(aliasName);
		if ( null == a) return aliasName;
		return a;
	}
	
	
}
