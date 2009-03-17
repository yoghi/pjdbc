package it.unibo.lmc.pjdbc.core;

import it.unibo.lmc.pjdbc.core.dml.ParsedCommand;

public interface IDatabase {

	IDatabase getSnapshot();
	
	boolean joinSnapshot(IDatabase snapshot);

	void applyCommand(ParsedCommand request);

}
