package net.prolog.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class SqlTest {

	static String rootWin = "/Users/Yoghi/Workspace/Java/Pjdbc/";
	static String rootMac = "C:\\Documents and Settings\\Administrator\\workspace\\Pjdbc\\";

	static String fileName =  "database/test.db";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		try {
			
			Class.forName("net.prolog.jdbc.PrologDriver");
			
			String systemOs = System.getProperty("os.name");
			
			System.out.println(System.getProperty("os.name"));
			
			Connection conn = null;
			
			if ( systemOs == "Windows XP" ) {
				conn = DriverManager.getConnection("jdbc:prolog:"+rootWin+fileName);
			} else {
				conn = DriverManager.getConnection("jdbc:prolog:"+rootMac+fileName);
			}
			
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select employee.name from employee,dept;");
			
			while(rs.next()) {
				String id = rs.getString("id");
				System.out.println("ID: "+id.toString());
			}
			
			//stmt.executeUpdate("set employee.name = 'pippo' to employee where employee.name = 'pippa' ");
			
			conn.close();
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found: "+e.toString());
		} catch (SQLException e) {
			System.out.println("SQL error: "+e.toString());
		}
		

	}

}
