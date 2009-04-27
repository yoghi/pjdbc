package it.unibo.lmc.pjdbc;

import it.unibo.lmc.pjdbc.driver.PrologConnection;
import it.unibo.lmc.pjdbc.driver.PrologStatement;

import java.io.File;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class Main {

	public static void main(String[] args) {

		
		load_config();
	    
//	    try {
//			PSchema psh = new PSchema("target/classes/prolog.db");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
		
			Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
		
			// SENZA METADATI
			
			//CONNECTION DEVE GESTIRE LE TRANSAZIONI TRA GLI STATEMENT DELLA STESSA CONNECTION
			//PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog.db");
			
			PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db");
			
			//PrologConnection conn = (PrologConnection)DriverManager.getConnection("jdbc:prolog:target/classes/prolog_with_meta.db;meta");
			
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(2);	//DEFAULT = 1
			
			PrologStatement stmt = (PrologStatement)conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select $0,$1 from employee;");

			ResultSet rs2 = stmt.executeQuery("select $0,$1,$2 from employee;");
			
			/*
			while(rs.next()) {
	            String name = rs.getString(0);
	            String name = rs.getString("name");
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

	private static void load_config() {
		/**
		 * Properties / custumization
		 */
		Properties properties = new Properties();
		
	    File propFile = new File("/mnt/store/workspace/Java/Pjdbc/target/classes/prolog.db.properties");
		//File propFile = new File("/Users/Yoghi/Workspace/Java/Pjdbc/target/classes/prolog.db.properties");
	    
	    // carico eventuali opzioni
	    if ( propFile.exists() ) {
	    	try {
	    		properties.load(new FileInputStream(propFile));
	    	} catch (Exception e) {
	    		System.out.println("><"+e.getLocalizedMessage());
			}
	    	
	    }
	    
	    PropertyConfigurator.configure(properties);
		
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
