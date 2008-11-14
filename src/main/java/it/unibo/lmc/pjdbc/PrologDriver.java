/*
 * JDBC 4.0 
 * Type 3
      Type 3 drivers provide a client with a generic network API that is then translated into
      database-specific access at the server level. In other words, the JDBC driver on the client
      uses sockets to call a middleware application on the server that translates the client requests
      into an API specific to the desired driver. As it turns out, this kind of driver is extremely
      flexible, since it requires no code installed on the client and a single driver can actually
      provide access to multiple databases.
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
			/* non può generare eccezioni */
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
