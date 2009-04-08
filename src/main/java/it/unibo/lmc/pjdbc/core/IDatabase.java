package it.unibo.lmc.pjdbc.core;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import it.unibo.lmc.pjdbc.driver.PrologResultSet;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

public interface IDatabase {

	IDatabase getSnapshot();
	
	boolean joinSnapshot(IDatabase snapshot);

	PrologResultSet applyCommand(Select request) throws SQLException;
	int applyCommand(Insert request) throws SQLException;
	int applyCommand(Update request) throws SQLException;
	int applyCommand(Delete request) throws SQLException;

	DatabaseMetaData getMetaData();

}
