package it.unibo.lmc.pjdbc.core;

import java.io.IOException;
import java.util.ArrayList;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class testPrologDB extends TestCase {

	private Theory th;

	/**
	 * Costruttore 
	 * @param testName
	 */
	public testPrologDB(String testName) {
		super(testName);
	}

	/**
	 * Setup
	 */
	protected void setUp() throws Exception {	
		super.setUp();
		
		th = null;
 		
 		
 		th = new Theory("pippo(0,1). " +
			"pippo(1,2). " +
			"pippo(2,3). ");
 		
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite();
		ts.addTest(new testPrologDB("testMalformedDB"));
		ts.addTest(new testPrologDB("testExecutionSelect"));
		ts.addTest(new testPrologDB("testExecutionInsert"));
		ts.addTest(new testPrologDB("testExecutionInsertMultiple"));
		//ts.addTest(new testPrologDB("testSavePoint"));
		//ts.addTest(new testPrologDB("testRestorePoint"));
		return ts;
	}
	
	/**
	 * TEST: seleziono delle righe dal db 
	 * @throws InvalidTheoryException 
	 */
	public void testMalformedDB() throws InvalidTheoryException {
		
		System.out.println(" ====================== ");
		System.out.println("  testMalformedDB       ");
 		System.out.println(" ====================== ");
		
 		Theory thMalf = new Theory("prova(1,_).");
 		
 		PrologLocalDB database = new PrologLocalDB("malformed",thMalf);
 		
	}
	
	
	/**
	 * TEST: seleziono delle righe dal db 
	 */
	public void testExecutionSelect() {
		
		System.out.println(" ====================== ");
		System.out.println("  testExecutionSelect   ");
 		System.out.println(" ====================== ");
		
		PrologLocalDB database = new PrologLocalDB("dbTest",th);
		
		class SelectRequest implements IRequest{
			public String toString(){
				return "pippo(X,Y).";
			}
		}
		
		ArrayList<SolveInfo> ret = database.executeRequest( new SelectRequest() );
		
		assertSame(3,ret.size());
		
		try {
			for(SolveInfo info : ret){
				System.out.println("soluzione "+info.getSolution());
			}
		} catch (NoSolutionException e) {
			fail(e.getLocalizedMessage());
		}
		
		
	}
	
	/**
	 * TEST: inserisco una riga nel db 
	 */
	public void testExecutionInsert(){
		
		System.out.println(" ====================== ");
		System.out.println("  testExecutionInsert   ");
 		System.out.println(" ====================== ");
		
 		PrologLocalDB database = new PrologLocalDB("dbTest",th);
		
		class InsertRequest implements IRequest{
			public String toString(){
				return "assert(pippo(3,4)).";
			}
		}
		
		ArrayList<SolveInfo> ret = database.executeRequest( new InsertRequest() );
	
		//NB: se stampo th non ottengo nulla in quanto è solo la config iniziale
		//System.out.println(database);
		
		assertSame(1,ret.size());
		
	}
	
	/**
	 * TEST: inserisco delle righe nel db 
	 */
	public void testExecutionInsertMultiple(){
		
		System.out.println(" ============================== ");
		System.out.println("  testExecutionInsertMultiple   ");
 		System.out.println(" ============================== ");
		
 		PrologLocalDB database = new PrologLocalDB("dbTest",th);
		
		class InsertRequest implements IRequest{
			public String toString(){
				return "assert(pippo(3,4));" +
						"\n" +
						"assert(pippo(4,5)).";
			}
		}
		
		ArrayList<SolveInfo> ret = database.executeRequest( new InsertRequest() );
	
		//NB: se stampo th non ottengo nulla in quanto è solo la config iniziale
		System.out.println(database);
		
		assertSame(2,ret.size());
		
	}
	
}