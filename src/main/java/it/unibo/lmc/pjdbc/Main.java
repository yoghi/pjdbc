package it.unibo.lmc.pjdbc;

import it.unibo.lmc.pjdbc.driver.PrologStatement;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class Main {

	public static void main(String[] args) {

		load_config();
		
		try {
		
			Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
		
			//jdbc:typeJdbcDriver:catalog dir/remote:default schema
			String user_dir = System.getProperty("user.dir");
			Connection conn = DriverManager.getConnection("jdbc:prolog:"+user_dir+"/target/classes/database/catalog1:prolog1");
			
			
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(2);	//DEFAULT = 1
			
			PrologStatement stmt = (PrologStatement)conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select e.*,d.* from employee as e, dept as d where d.id = e.id;");
			
			outputResultSet(rs);
			
//			conn.commit();
			
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void outputResultSet(ResultSet rs) throws Exception {
	    
		ResultSetMetaData rsMetaData = rs.getMetaData();
	    int numberOfColumns = rsMetaData.getColumnCount();
	    for (int i = 1; i < numberOfColumns + 1; i++) {
	      String columnName = rsMetaData.getColumnLabel(i);
	      
	      System.out.print(columnName + "   ");
	
	    }
	    System.out.println();
	    System.out.println("----------------------");
	
	    while (rs.next()) {
	      for (int i = 1; i < numberOfColumns + 1; i++) {
	        System.out.print(rs.getString(i) + "   ");
	      }
	      System.out.println();
	    }

	}
	
	private static void load_config() {
	    /**
		 * Properties / custumization
		 */
		Properties properties = new Properties();
		
		String userDir = System.getProperty("user.dir");
		File propFile = new File(userDir + "/target/classes/common.properties");
		
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

//ResultSet rs22 = stmt.executeQuery("select id from employee where (id = 1);");
//
//ResultSet rs2bis = stmt.executeQuery("select id,idRef from employee where (id = idRef);");	//cerco i figli che sono anche padri...
//
//ResultSet rs2bis3 = stmt.executeQuery("select id from employee where (id = 1) ;");
//
//ResultSet rs2bis2 = stmt.executeQuery("select id,idRef from employee where ( (id = 1) AND (idRef > 2) );");
//
//ResultSet rs2bis4 = stmt.executeQuery("select id,idRef from employee where ( ( (id = 1) OR (idRef > 2) ) AND (idRef + 1) ) limit 5 ;");
//
//ResultSet rs3 = stmt.executeQuery("select * from employee limit 5;");
//
//ResultSet rs4 = stmt.executeQuery("select * from employee limit 5,15;");

//ResultSet rs = stmt.executeQuery("select $0,$3 from employee;");

//ResultSet rs3 = stmt.executeQuery("select pippo from employee;");

//ResultSet rs2 = stmt.executeQuery("select $0,$1,$2 from employee;");

//ResultSet rs5 = stmt.executeQuery("select $0 as pippo,$1,$2 from employee;");

//ResultSet rs4 = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d;");

//ResultSet rs6 = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where (e.$0 = d.$1);");

//ResultSet rs7 = stmt.executeQuery("select e.$0,e.$1,d.$1 from employee as e, dept as d where (e.$0 = (7 * 3 + d.$1) );");

/*
while(rs.next()) {
    String name = rs.getString(0);
    String name = rs.getString("name");
    System.out.println("$0 = "+name.toString());
}
*/
