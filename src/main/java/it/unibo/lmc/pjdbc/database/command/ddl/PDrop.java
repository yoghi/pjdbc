package it.unibo.lmc.pjdbc.database.command.ddl;

import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.meta.MTable;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.imp.Drop;
import it.unibo.lmc.pjdbc.parser.schema.Table;

public class PDrop extends PRequest {

	public PDrop(MSchema ms, ParsedCommand req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws PSQLException {
		
		//DROP TABLE - retract(tablename(_,_,_)).
		StringBuilder tempBuild = new StringBuilder();
		
		Drop dropReq = (Drop)this.mcommand;
		
		Table[] tables = dropReq.getTablesList().get(this.mschema.getSchemaName());
		
		for (Table table : tables) {
			
			tempBuild.append("retract");
			tempBuild.append("(");
			
			tempBuild.append(table.getName());
			tempBuild.append("(");
			MTable mInfo = this.mschema.getMetaTableInfo(table.getName());
			for (int i = 0; i < mInfo.numColum(); i++) {
				tempBuild.append("_");
				tempBuild.append(",");
			}
			tempBuild.reverse();
			tempBuild.replace(0,1,")");
			tempBuild.reverse();
			
			tempBuild.append(")");
			tempBuild.append(";");
			
		}
		tempBuild.reverse();
		tempBuild.replace(0, 1, ".");
		tempBuild.reverse();
		
		return tempBuild.toString();
	}

}
