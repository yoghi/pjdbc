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
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
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
				
				int pos = 0;
				if ( field.getColumnName().startsWith("$") ){
					pos = Integer.parseInt(field.getColumnName().replace("$", "")); 
				} else {
					pos = mTable.findField(field.getColumnName());
				}
				
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
	 * @throws PSQLException 
	 */
	protected String generateWhereClausole() throws PSQLException {
		
		StringBuilder builder = new StringBuilder();
		
		Expression exp = ((Update)this.mcommand).getWhereClausole();
		
		if ( exp == null ) return "";
		
		this.analizeExpression(exp,builder);
		
		return builder.toString();
		
	}
	
	protected void analizeExpression(Expression exp, StringBuilder builder) throws PSQLException{
		
		if ( exp.getCondition() != null ){ // 1. Left Condition Right
			
			builder.append("(");
			this.analizeExpression(exp.getLeftExpression(),builder);
			builder.append(exp.getCondition().toString());
			this.analizeExpression(exp.getRightExpression(),builder);
			builder.append(")");
			
		} else { //2. Left
			
			//Expression
			if ( exp.getLeftExpression() != null ){
				
				this.analizeExpression(exp.getLeftExpression(),builder);
			
			} else {
				
				if ( exp.getLeft() != null ){
					
					builder.append(exp.getLeft());
					
				} else {
					
					TableField tf = exp.getLeftF();
					
					if ( tf.getSchema() == null ) tf.setSchema(this.mschema.getSchemaName());
					if ( tf.getTableName() == null ) tf.setTableName(this.updateTable.getName());
					
					String varPsql = null;
					
					if ( tf.getColumnName().startsWith("$") ){
						MTable info = this.mschema.getMetaTableInfo(tf.getTableName());
						MColumn[] columns = info.getColumns();
						int pos = Integer.parseInt(tf.getColumnName().replace("$", ""));
						for(String key : this.mapVariables.keySet() ){
							if ( this.mapVariables.get(key).equalsIgnoreCase(columns[pos].getQualifiedName()) ){
								varPsql = key;
							}
						}
					} else {
				
						for(String key : this.mapVariables.keySet() ){
							if ( this.mapVariables.get(key).equalsIgnoreCase(tf.getQualifiedName()) ){
								varPsql = key;
							}
						}
					}
					
					builder.append(varPsql);
					
				}
				
			}
					
		}
		
	}

}
