package it.unibo.lmc.pjdbc.driver;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DriverTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for it.unibo.lmc.pjdbc.driver");
		//$JUnit-BEGIN$
		suite.addTestSuite(PrologMetaDataTest.class);
		suite.addTestSuite(PrologResultSetTest.class);
		suite.addTestSuite(PrologStatementTest.class);
		//$JUnit-END$
		return suite;
	}

}
