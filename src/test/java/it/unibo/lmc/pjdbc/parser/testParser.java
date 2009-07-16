package it.unibo.lmc.pjdbc.parser;

import it.unibo.lmc.pjdbc.parser.dml.ParsedCommand;
import it.unibo.lmc.pjdbc.parser.dml.expression.Expression;
import it.unibo.lmc.pjdbc.parser.dml.imp.Select;
import it.unibo.lmc.pjdbc.parser.schema.Table;
import it.unibo.lmc.pjdbc.parser.schema.TableField;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.PropertyConfigurator;

public class testParser extends TestCase {

	/** 
	 * Costruttore 
	 * @param testName
	 */
	public testParser(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}
	

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		
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
		
		TestSuite ts = new TestSuite();
		 
		ts.addTest(new testParser("testSelectMultiTable"));
		ts.addTest(new testParser("testInsert"));
		ts.addTest(new testParser("testAnyClausola"));
		ts.addTest(new testParser("testCreateTable"));
		ts.addTest(new testParser("testSelectWhere"));
		
		ts.addTest(new testParser("testNullInsert"));
		
		ts.addTest(new testParser("testSelectWhereOR"));
		ts.addTest(new testParser("testSelectWhereOR2"));
		
		ts.addTest(new testParser("testSelectWhere2"));
		
		return ts;
	}
	
	 
	/**
	 * TEST: verifico la corretta visione di più tabelle nella select 
	 */
	public void testSelectMultiTable(){
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectMultiTable  ");
 		System.out.println(" ====================== ");
 		
 		try {
			
			String query = "select e.$0,e.$1,d.$1 from employee as e, dept as d; ";
			
			ParsedCommand pRequest = null;
			
			Psql parse = new Psql(new StringReader(query));
			pRequest = parse.parseIt();
			
			if ( pRequest instanceof Select ) {
				
				Select pSelect = (Select)pRequest;
				
				List<Table> tables = pSelect.getFromTable();
				
				Table[] tablesArray = new Table[2];
				tablesArray = tables.toArray(tablesArray);
				
				if ( tablesArray.length != 2 ){
					fail("not detect correctly tables");
				}
				
				if ( !tablesArray[0].getName().equalsIgnoreCase("employee") || !tablesArray[0].getAlias().equals("e") ){
					fail("not detect tables employee");
				}
				
				if ( !tablesArray[1].getName().equalsIgnoreCase("dept") || !tablesArray[1].getAlias().equals("d") ){
					fail("not detect tables dept");
				}
				
				List<TableField> campi = pSelect.getCampiRicerca();
				TableField[] campiArray = new TableField[2];
				campiArray = campi.toArray(campiArray);
				
				if ( campiArray.length != 3 ){
					fail("not detect correctly fields");
				}
				
				if ( !campiArray[0].getColumnName().equalsIgnoreCase("$0") || null == campiArray[0].getTableName() || !campiArray[0].getTableName().equalsIgnoreCase("e") ){
					fail("not detect field e.$0");
				}
				
				if ( !campiArray[1].getColumnName().equalsIgnoreCase("$1") || null == campiArray[1].getTableName() || !campiArray[1].getTableName().equalsIgnoreCase("e") ){
					fail("not detect field e.$1");
				}
				
				if ( !campiArray[2].getColumnName().equalsIgnoreCase("$1") || null == campiArray[2].getTableName() || !campiArray[2].getTableName().equalsIgnoreCase("d") ){
					fail("not detect field d.$1");
				}
				
				
			} else {
				fail("not detect select");
			}
			
			System.out.println(pRequest);
			
		} catch (Exception e) {
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}

	/**
	 * 
	 */
	public void testSelectWhere(){
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhere    ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "select * from employee as e, dept as d where e.id > 0;";
			
			ParsedCommand pRequest = null;
			
			Psql parse = new Psql(new StringReader(query));
			pRequest = parse.parseIt();
			
			System.out.println(pRequest);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
	}
	
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhereOR() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhereOR    ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "select e.$1,d.$0 from employee as e , dept as d , eta as et where  (e.$0 = d.$1) AND ( e.$1 = et.$0 )  AND  (et.$1 < 40) OR ( e.$2 > 2000 ) ;";
			
			System.out.println(query);
			
			ParsedCommand pRequest = null;
			
			Psql parse = new Psql(new StringReader(query));
			pRequest = parse.parseIt();
			
			Select sRequest = (Select)pRequest;
			
			Expression exp = sRequest.getWhereClausole();
			
			// (e.$0 = d.$1) AND ( e.$1 = et.$0 )  AND  (et.$1 < 40) OR ( e.$2 > 2000 )
			// E1 AND E2 AND E3 OR E4
			Expression E123 = exp.getLeftExpression();
			
			Expression E4 = exp.getRightExpression();
			System.out.println("E4 : ( e.$2 > 2000 ) "+E4.toString());
			
			Expression E3 = E123.getRightExpression();
			System.out.println("E3 : (et.$1 < 40) "+E3.toString());
			
			Expression E12 = E123.getLeftExpression();
			
			Expression E2 = E12.getRightExpression();
			System.out.println("E2 : ( e.$1 = et.$0 )  "+E2.toString());
			
			Expression E1 = E12.getLeftExpression();
			System.out.println("E1 : (e.$0 = d.$1)  "+E1.toString());
			
			
		} catch (Exception e) {
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhereOR2() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhereOR2    ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "select e.$1,d.$0 from employee as e , dept as d , eta as et where ( ( (e.$0 = d.$1) AND ( e.$1 = et.$0 ) ) AND ( (et.$1 < 40) OR ( e.$2 > 2000 ) ) ) ;";
		
			System.out.println(query);
			
			ParsedCommand pRequest = null;
			
			Psql parse = new Psql(new StringReader(query));
			pRequest = parse.parseIt();
			
			Select sRequest = (Select)pRequest;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testSelectWhere2() {
		
		System.out.println(" ====================== ");
		System.out.println("  testSelectWhere2      ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "select e.$1,d.$0 from employee as e , dept as d , eta as et where ( e.$1 > 2 ) < 7000 ;";
		
			System.out.println(query);
			
			ParsedCommand pRequest = null;
			
			Psql parse = new Psql(new StringReader(query));
			pRequest = parse.parseIt();
			
			Select sRequest = (Select)pRequest;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST: Select di un campo specifico
	 */
	public void testInsert() {
		
		System.out.println(" ====================== ");
		System.out.println("  testInsert    ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "insert into employee (id,name,salary) values (1,'babo',1000);";
			
			ParsedCommand pRequest = null;
			
			Psql parse = new Psql(new StringReader(query));
			pRequest = parse.parseIt();
			
			System.out.println(pRequest);
			
			
		} catch (Exception e) {
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST Select con in mezzo ANY
	 */
	public void testAnyClausola() {
		
		System.out.println(" ====================== ");
		System.out.println("  testAnyClausola       ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "select e.*,d.$1 from employee as e, dept as d;";
			
			ParsedCommand pRequest = null;
			
			Psql parse = new Psql(new StringReader(query));
			pRequest = parse.parseIt();
			
			System.out.println(pRequest);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST Select con in mezzo ANY
	 */
	public void testCreateTable() {
		
		System.out.println(" ====================== ");
		System.out.println("  testCreateTable       ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "create table prova ( id varchar(255) , age int ); ";
		
			Psql parse = new Psql(new StringReader(query));
			
			ParsedCommand pRequest = parse.parseIt();
			
			System.out.println(pRequest);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}
	
	/**
	 * TEST Select con in mezzo ANY
	 */
	public void testNullInsert() {
		
		System.out.println(" ====================== ");
		System.out.println("  testNullInsert        ");
 		System.out.println(" ====================== ");
		
		try {
			
			String query = "insert into prova values (null,1); ";
		
			Psql parse = new Psql(new StringReader(query));
			
			ParsedCommand pRequest = parse.parseIt();
			
			System.out.println(pRequest);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(" Parser error: " + e);
		}
		
		assertTrue(true);
		
	}

}
