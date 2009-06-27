package it.unibo.lmc.pjdbc.core.command.dml;

import it.unibo.lmc.pjdbc.core.command.PResultSet;
import it.unibo.lmc.pjdbc.core.utils.PSQLException;
import it.unibo.lmc.pjdbc.parser.dml.imp.Delete;
import it.unibo.lmc.pjdbc.parser.dml.imp.Insert;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.dml.imp.Update;

import java.sql.SQLException;


public interface IDml {

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
	 * DELETE
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	int applyCommand(Delete request) throws PSQLException;
	
}