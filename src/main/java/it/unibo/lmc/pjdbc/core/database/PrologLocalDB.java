package it.unibo.lmc.pjdbc.core.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import alice.tuprolog.InvalidTheoryException;

/**
 * Database Prolog
 * In questo classe ho 
 * 		un meccanismo di lock per garantire il corretto accesso alla risorsa
 * 		uno di commit per la conferma dei cambiamenti
 * 		uno di savePoint  per poter salvare lo stato in un dato momento 
 */
public class PrologLocalDB extends PrologDB {

	/**
	 * Properties / custumization
	 */
	private Properties properties = new Properties();

	/**
	 * Logger 
	 */
	private Logger log = null;
	
	/**
	 * Reader of prolog file
	 */
	BufferedReader input;
	
	
	/**
	 * Database locale scritto in prolog 	
	 * @param sourceUrl path del file contenente il db prolog
	 * @throws IOException
	 * @throws InvalidTheoryException 
	 */
	public PrologLocalDB(String sourceUrl) throws IOException, InvalidTheoryException {
		
		File filePrologDB = new File(sourceUrl);
		boolean exists = filePrologDB.exists();
	    
	    if ( exists ) {
	    	String userDir = System.getProperty("user.dir");
	    	if ( !(new File(userDir+File.separator+sourceUrl)).exists() ){
	    		throw new FileNotFoundException("File "+sourceUrl+" and "+userDir+File.separator+sourceUrl+" doesn't exist");
	    	} else {
	    		sourceUrl = userDir + File.separator + sourceUrl;
	    	}
	    } else {
	    	System.out.println("Impossibile caricare il db: "+filePrologDB.getAbsolutePath());
	    	boolean success = filePrologDB.createNewFile();
	    	if ( success ) System.out.println("Creato database vuoto: "+filePrologDB.getAbsolutePath());
	    	else throw new IOException("Impossibile creare: "+filePrologDB.getAbsolutePath());
	    }
	    
	    input = new BufferedReader(new FileReader(filePrologDB));
	    
	    File propFile = new File(sourceUrl+".properties");
	    
	    // carico eventuali opzioni
	    if ( propFile.exists() ) {
	    	try {
	    		properties.load(new FileInputStream(propFile));
	    	} catch (Exception e) {
	    		System.out.println("><"+e.getLocalizedMessage());
			}
	    	
	    }
		
		this.logger_init();
		
		this.load_theory();
		
		this.readMeta();
		
	}

	/**
	 * Inizializzo il sistema di logging
	 */
	protected void logger_init() {
		PropertyConfigurator.configure(properties);
		log = Logger.getLogger("it.unibo.lmc.pjdbc");
	}

	@Override
	protected String readRow(int numLine) {
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
		
	}

	@Override
	protected String writeRow(int numLine, String newVal) {
		// TODO DA PENSRE BENE COME SCRIVERE UNA MODIFICA .....
		return null;
	}
	

}
