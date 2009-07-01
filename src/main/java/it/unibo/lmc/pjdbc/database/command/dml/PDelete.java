package it.unibo.lmc.pjdbc.database.command.dml;

import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.schema.Table;

public class PDelete extends PRequest {

	public PDelete(MSchema ms, ParsedCommand req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws PSQLException {

		//DELETE FROM table_name WHERE (expression) 
		
		Table table = ((Delete)this.mcommand).getTable();
		
		MTable mTable = this.mschema.getMetaTableInfo(table.getName());
		
		Expression where = ((Delete)this.mcommand).getWhereClausole();
		
		
		
		return null;
	}

}
