package it.unibo.lmc.pjdbc.database.command.dml;

import it.unibo.lmc.pjdbc.database.command.PClausola;
import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.meta.MColumn;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.HashMap;

public class PUpdate extends PRequest {

	private Table updateTable;

	public PUpdate(MSchema ms, ParsedCommand req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws PSQLException {
		
		StringBuilder build = new StringBuilder();
		
		// UPDATE table_name SET column1=value, column2=value2,... WHERE some_column=some_valu
		
		// table_name(X1,X2,X3),X1=1,retract(table_name(X1,X2,X3)),assert(table_name(N1,N2,N3)).
		
		Update pUpdage = (Update)this.mcommand;
		
		updateTable = pUpdage.getTable();
		MTable mTable = this.mschema.getMetaTableInfo(updateTable.getName());
		PClausola clausola = new PClausola(mTable);
		MColumn[] columns = mTable.getColumns();
		
		for (int i = 0; i < mTable.numColum(); i++) {
			String var = this.generateNewVar( columns[i].getQualifiedName() );
			clausola.setTerm(var, i, true);
		}
		
		build.append(clausola.toString());
		build.append(",");
		
		String whereClausole = this.generateWhereClausole();
		if ( !whereClausole.equalsIgnoreCase("") ) {
			build.append(whereClausole);
			build.append(",");
		}
		
		build.append("retract");
		build.append("(");
		build.append(clausola.toString());
		build.append(")");
		build.append(",");
		
		HashMap<TableField, String> accoppiamneti = pUpdage.getUpdates();
		for (TableField field : accoppiamneti.keySet()) {
			
			if ( field.getTableName() == null ) field.setTableName(updateTable.getName());
			
			if ( field.getTableName().equalsIgnoreCase(mTable.getTableName()) ){
				
				int pos = mTable.containsField(field.getColumnName());
				clausola.setTerm(accoppiamneti.get(field), pos, true);
				
			} else throw new PSQLException("clausola "+field.toString()+" "+accoppiamneti.get(field), PSQLState.INVALID_CLAUSOLE);
		}
		
		build.append("assert");
		build.append("(");
		build.append(clausola.toString());
		build.append(")");
		build.append(".");

		return build.toString();
	}
	
	/**
	 * Genero le clausole riguardanti il campo WHERE
	 */
	protected String generateWhereClausole() {
		
		Expression exp = ((Update)this.mcommand).getWhereClausole();
		
		if ( exp == null ) return "";
		
		StringBuilder build = new StringBuilder();
		
		if ( null != exp.getLeft() ) {
			build.append(exp.getLeft());
		} else {
			if ( null != exp.getLeftF() ) {
				
				TableField tf = exp.getLeftF();
				if ( tf.getSchema() == null ) tf.setSchema(this.mschema.getSchemaName());
				if ( tf.getTableName() == null ) tf.setTableName(this.updateTable.getName());
				String varPsql = null;
				for(String key : this.mapVariables.keySet() ){
					if ( this.mapVariables.get(key).equalsIgnoreCase(tf.getQualifiedName()) ){
						varPsql = key;
					}
				}
				
				build.append(varPsql);
				
			}
		}
		
		build.append(exp.getCondition().toString());
		
		if ( null != exp.getRight() ) {
			build.append(exp.getRight());
		} else {
			if ( null != exp.getRightF() ) {
				
				TableField tf = exp.getRightF();
				
				String varPsql = null;
				for(String key : this.mapVariables.keySet() ){
					if ( this.mapVariables.get(key).equalsIgnoreCase(tf.getQualifiedName()) ){
						varPsql = key;
					}
				}
				
				build.append(varPsql);
				
			}
		}
		
		return build.toString();
		
	}

}
