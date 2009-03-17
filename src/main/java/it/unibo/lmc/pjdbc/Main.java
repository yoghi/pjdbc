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
			
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(2);	//DEFAULT = 1
			
			PrologStatement stmt = (PrologStatement)conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select $0,$1 from employee;");

			/*
			while(rs.next()) {
	            String name = rs.getString(0);
	            System.out.println("$0 = "+name.toString());
			}
			*/
			conn.commit();
			
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}

//ResultSet rs21bis = stmt.executeQuery("select id from employee where true;");
//
//ResultSet rs21 = stmt.executeQuery("select id from employee where (true);");

//ResultSet rs22 = stmt.executeQuery("select id from employee where (id == 1);");
//
//ResultSet rs2bis = stmt.executeQuery("select id,idRef from employee where (id == idRef);");	//cerco i figli che sono anche padri...
//
//ResultSet rs2bis3 = stmt.executeQuery("select id from employee where (id == 1) ;");
//
//ResultSet rs2bis2 = stmt.executeQuery("select id,idRef from employee where ( (id == 1) AND (idRef > 2) );");
//
//ResultSet rs2bis4 = stmt.executeQuery("select id,idRef from employee where ( ( (id == 1) OR (idRef > 2) ) AND (idRef + 1) ) limit 5 ;");
//
//ResultSet rs3 = stmt.executeQuery("select * from employee limit 5;");
//
//ResultSet rs4 = stmt.executeQuery("select * from employee limit 5,15;");
