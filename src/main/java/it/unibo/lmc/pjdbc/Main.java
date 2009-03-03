package it.unibo.lmc.pjdbc;

import it.unibo.lmc.pjdbc.driver.PrologConnection;
import it.unibo.lmc.pjdbc.driver.PrologStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {

		
		try {
		
			Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
		
			// SENZA METADATI
			
			PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
			
			//PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db");
			
			//PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db;meta");
			
			PrologStatement stmt = (PrologStatement)conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select $0,$1 from employee;");
			
			ResultSet rs2 = stmt.executeQuery("select id from employee;");
			
			/*
			while(rs.next()) {
	            String name = rs.getString("name");
	            System.out.println("NAME: "+name.toString());
			}
			*/
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
