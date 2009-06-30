package it.unibo.lmc.pjdbc.database.command.ddl;

import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

public class PDrop extends PRequest {
	
	

	public PDrop(MSchema ms, ParsedCommand req) {
		super(ms, req);
	}

	@Override
	public String generatePrologRequest() throws PSQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
