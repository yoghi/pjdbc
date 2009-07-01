package it.unibo.lmc.pjdbc.database.command;

import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Drop;
import it.unibo.lmc.pjdbc.parser.dml.imp.DropDB;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

import java.sql.SQLException;


public interface ICommnad {

	/**
	 * SELECT
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	PResultSet applyCommand(Select request) throws PSQLException;
	
	/**
	 * INSERT
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	int applyCommand(Insert request) throws PSQLException;
	
	/**
	 * UPDATE
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	int applyCommand(Update request) throws PSQLException;
	
	/**
	 * DELETE row
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	int applyCommand(Delete request) throws PSQLException;
	
	/**
	 * DROP TABLES
	 * @param request
	 * @throws PSQLException
	 */
	int applyCommand(Drop request) throws PSQLException;
	
	/**
	 * DROP DATABASE
	 * @param request
	 * @throws PSQLException
	 */
	void applyCommand(DropDB request) throws PSQLException;
	
}
