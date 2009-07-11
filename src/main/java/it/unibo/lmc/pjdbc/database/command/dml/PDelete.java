package it.unibo.lmc.pjdbc.database.command.dml;

import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.List;

public class PDelete extends Pselect {

	public PDelete(MSchema ms, Select req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws PSQLException {
		
		Delete pDelete = (Delete)this.mcommand;
		
		String tableName = pDelete.getFromTable().get(0).getName();
		
		List<TableField> campi = pDelete.getCampiRicerca();
		campi.clear();
		TableField t = new TableField("*");
		t.setSchema(this.mschema.getSchemaName());
		campi.add( t );
		
		//employee(IMP0VAR,IMP1VAR,_),dept(_,IMP2VAR),IMP0VAR=:=IMP2VAR.
		this.generateAlias();
		
		this.anyClausoleConvert();
		
		this.generateFromClausole();
		
		if ( pDelete.getWhereClausole().getLeftF() != null ) {
			pDelete.getWhereClausole().getLeftF().setTableName(tableName);
		}
		
		if ( pDelete.getWhereClausole().getRightF() != null ) {
			pDelete.getWhereClausole().getRightF().setTableName(tableName);
		}
		
		//where clausole
		String whereClausole = this.generateWhereClausole();
		
		StringBuilder sbuilder = new StringBuilder();
		for( String key : this.clausole.keySet()){
			
			sbuilder.append(this.clausole.get(key).toString());
			sbuilder.append(",");
			
		}
		if ( !whereClausole.equalsIgnoreCase("") ) {
			sbuilder.append(whereClausole);
		}
		
		sbuilder.append(",");
		//ora devo aggiungere le retract! retract(employee(IMP0VAR,IMP1VAR,_)).
		for( String key : this.clausole.keySet()){
			
			sbuilder.append("retract(");
			sbuilder.append(this.clausole.get(key).toString());
			sbuilder.append(")");
			sbuilder.append(",");
		}
		sbuilder.reverse();
		sbuilder.replace(0, 1, ".");
		sbuilder.reverse();
		
		return sbuilder.toString(); 
		
	}

}
