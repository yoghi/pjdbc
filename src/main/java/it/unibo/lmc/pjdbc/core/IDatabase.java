package it.unibo.lmc.pjdbc.core;

import it.unibo.lmc.pjdbc.core.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.driver.PrologResultSet;

public interface IDatabase {

	IDatabase getSnapshot();
	
	boolean joinSnapshot(IDatabase snapshot);

	void applyCommand(ParsedCommand request,PrologResultSet result);

}
