package it.unibo.lmc.pjdbc.database.meta;

import it.unibo.lmc.pjdbc.database.utils.PSQLException;
import it.unibo.lmc.pjdbc.database.utils.PSQLState;
import it.unibo.lmc.pjdbc.database.utils.PTypes;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class MTable {
	
	/**
	 * Schema
	 */
	private MSchema schema;
	
	/**
	 * Indice NomeColonna => posizione
	 * NB: due colonne con lo stesso nome non possono esistere!!!
	 */
	private Map<String,Integer> tcolumns;
	
	/**
	 * Elenco Colonne
	 */
	private MColumn[] columns;
	
	/**
	 * Nome tabella
	 */
	private String tname;
	
	/**
	 * Logger 
	 */
	private Logger log = null;
	
	/**
	 * Costruttore Tabella
	 * @param name nome tabella
	 * @param columNumber numero di colonne
	 */
	public MTable(MSchema schema, String name, int columNumber) {
		 this.tcolumns = new HashMap<String, Integer>();
		 this.columns = new MColumn[columNumber];
		 this.tname = name;
		 this.schema = schema;
		 log = Logger.getLogger("it.unibo.lmc.pjdbc.core.meta");
	}
	
	/**
	 * @return the table name
	 */
	public String getTableName() {
		return tname;
	}
	
	/**
	 * setto una colonna della tabella
	 * @param position
	 * @param columnName
	 * @param columnType
	 * @return
	 * @throws PSQLException
	 */
	public MColumn setField(int position, String columnName, String columnType) throws PSQLException {
		
		if (  position >= 0 && position < this.columns.length ) {	
			
			if ( null == this.tcolumns.put(columnName, position) ) {

				MColumn columnNew = new MColumn(this.schema, this, columnName, PTypes.valueOf(columnType.toUpperCase()));
				this.columns[position] = columnNew;
				log.debug("add new column "+columnName);
				return null;
				
			} else { //override
				
				MColumn columnOld = this.columns[position];
				MColumn columnNew = new MColumn(this.schema, this, columnName, PTypes.valueOf(columnType.toUpperCase()));
				
				this.columns[position] = columnNew;
				
				return columnOld;
			}
			
			
		} else {
			
			if ( position < 0 ) throw new PSQLException("posizione "+position + " non valida", PSQLState.INVALID_POSITION);
			
			this.tcolumns.put(columnName, position);
			MColumn columnNew = new MColumn(this.schema, this, columnName, PTypes.valueOf(columnType.toUpperCase()));
			
			try {
				
				int newsize = position+1;
				MColumn[] temp = new MColumn[newsize];
				
				for (int i = 0; i < this.columns.length; i++) {
					temp[i] = this.columns[i];
				}
				
				this.columns = temp;
				
			} catch (IndexOutOfBoundsException e) {
				throw new PSQLException("problemi nel resize della lista di colonne della tabella "+this.tname, PSQLState.SYSTEM_ERROR);
			}
			
			this.columns[position] = columnNew;
			log.debug("add new column "+columnName);
			return null;
		}
		
	}

	/**
	 * Ottengo la prima column che corrisponde al pattern passato 
	 * @param columnNamePattern
	 * @return column metainfo
	 * @throws PSQLException
	 */
	public MColumn getColumnMeta(String columnNamePattern) throws PSQLException {
		if ( null == columnNamePattern ) throw new PSQLException("column can't be null", PSQLState.INVALID_NAME);
		Integer pos = this.tcolumns.get(columnNamePattern);
		if ( null == pos ) throw new PSQLException("column "+columnNamePattern+" not exist", PSQLState.UNDEFINED_COLUMN);
		return this.columns[pos.intValue()];
	}
	
	/**
	 * Verifico se la colonna esiste in questa tabella
	 * @param columnName
	 * @return
	 * @throws PSQLException 
	 */
	public int containsField(String columnName) throws PSQLException {
		Integer pos = this.tcolumns.get(columnName);
		if ( null == pos ) throw new PSQLException("invalid column "+columnName, PSQLState.UNDEFINED_COLUMN);
		return pos;
	}

	/**
	 * Restituisco quante colonne ha questa tabella
	 * @return numero colonne
	 */
	public int numColum() {
		return this.columns.length;
	}

	/**
	 * Visualizzo la tabella
	 */
	public String toString(){
		StringBuilder build = new StringBuilder();
		
		for (int i = 0; i < this.columns.length; i++) {
			
			Formatter formatter = new Formatter();
			
			if ( null == this.columns[i] ) {
				build.append("unknow \t unknow \n");
			} else {
				formatter.format(" %s \t %s  \n",this.columns[i].getColumnName(),this.columns[i].getColumnType());
				build.append(formatter.toString());
			}
			
		}
		
		return build.toString();
	}

	/**
	 * Array ordinata per posizione di tutte le colonne della tabella.
	 * @return array of MColumns
	 */
	public MColumn[] getColumns() {
		return this.columns.clone();
	}
	
	
}