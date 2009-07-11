package it.unibo.lmc.pjdbc.database.command.dml;

import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.util.List;

public class PInsert extends PRequest {

	
	
	public PInsert(MSchema ms, ParsedCommand req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws PSQLException {
		
		StringBuilder build = new StringBuilder();
		
		build.append("assert");
		build.append("(");
		
		Insert insertCommand = (Insert)this.mcommand;
		
		Table tname = insertCommand.getTable();
		MTable infoTable = this.mschema.getMetaTableInfo(tname.getName());
		
		if ( null == infoTable ) throw new PSQLException("invalid table "+tname.getName(), PSQLState.UNDEFINED_TABLE);
		
		build.append(tname.getName());
		build.append("(");
		
		String[]  request = new String[infoTable.numColum()];
		
		List<TableField> fields = insertCommand.getFields();
		List<String> values = insertCommand.getValues();
		
		if ( fields.size() > 0) {
			int i = 0;
			for (TableField field : fields) {
				int pos = infoTable.findField(field.getColumnName());
				request[pos] = values.get(i);
				i++;
			}
		} else {
			if ( infoTable.numColum() != values.size() ) throw new PSQLException("numero valori errati", PSQLState.SYNTAX_ERROR);
			request = values.toArray(request);
		}
		
		for (int i = 0; i < request.length; i++) {
			build.append(request[i]);
			build.append(",");
		}
		
		build.reverse();
		build.replace(0, 1, ")");
		build.reverse();
		
		build.append(").");
		return build.toString();
	}

	
	
}
