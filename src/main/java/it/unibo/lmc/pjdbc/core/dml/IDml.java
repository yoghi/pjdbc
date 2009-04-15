package it.unibo.lmc.pjdbc.core.dml;

import it.unibo.lmc.pjdbc.driver.PrologResultSet;
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
	PrologResultSet applyCommand(Select request) throws SQLException;
	
	/**
	 * INSERT
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	int applyCommand(Insert request) throws SQLException;
	
	/**
	 * UPDATE
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	int applyCommand(Update request) throws SQLException;
	
	/**
	 * DELETE
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	int applyCommand(Delete request) throws SQLException;
	
}
