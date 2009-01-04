/*
 * JDBC 4.0 
 * 
   	Type 4 — drivers that are pure Java often using a network protocol or File I/O to 
			 communicate with a specific data source. The client connects directly to the data 
			 source.   
 */
package it.unibo.lmc.pjdbc;

import java.sql.*;
import java.util.*;

// jdbc:jdbcSDK:<sqlengine>:<parametro1>=<valore>:...:<parametroN>=valore
// jdbc:prolog:<filename>

public class PrologDriver implements Driver {

	private static final String PREFIX = "jdbc:prolog:";
	
	static {
		try {
			DriverManager.registerDriver(new PrologDriver());
		} catch (Exception e) {
			/* non può generare eccezioni (??) */
		}
	}

	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith(PREFIX);
	}

	public Connection connect(String url, Properties info) throws SQLException {
		
		if ( !acceptsURL(url) ) return null;
		url = url.trim();
		
		//FIXME: devo restituire la stessa Connection se richiedo di lavorare sullo stesso DB! Quindi 1 connection per Db!!
		return new PrologConnection(url, PREFIX.equalsIgnoreCase(url) ? ":memory:" : url.substring(PREFIX.length()) );
	}

	public int getMajorVersion() { return 1; }

	public int getMinorVersion() { return 1; }

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return null;
	}

	public boolean jdbcCompliant() {
		return false;
	}

}
