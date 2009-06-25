package it.unibo.lmc.pjdbc.core.ddl;

import it.unibo.lmc.pjdbc.core.database.PRequest;
import it.unibo.lmc.pjdbc.core.meta.MSchema;
import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.utils.PSQLException;

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
