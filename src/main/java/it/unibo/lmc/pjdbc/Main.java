package it.unibo.lmc.pjdbc;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {

		
		try {
		
			Class.forName("it.unibo.lmc.pjdbc.PrologDriver");
		
			// SENZA METADATI
			//PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
			PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db");
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
