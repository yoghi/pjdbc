package it.unibo.lmc.pjdbc.database.command.ddl;

import it.unibo.lmc.pjdbc.database.command.PRequest;
import it.unibo.lmc.pjdbc.database.meta.MSchema;
import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;

public class PCreate extends PRequest {

	public PCreate(MSchema ms, ParsedCommand req) {
		super(ms, req);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String generatePrologRequest() throws PSQLException {
		// TODO Auto-generated method stub
		// devo inserire metadati...
		return null;
	}

}
