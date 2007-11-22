package net.prolog.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class SqlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = "/tmp/databas.prolog";
		
		try {
			
			Class.forName("net.prolog.PrologDriver");
			
			Connection conn = DriverManager.getConnection("jdbc:prolog:"+fileName);
			
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
