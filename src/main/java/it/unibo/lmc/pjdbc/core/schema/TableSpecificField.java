package it.unibo.lmc.pjdbc.core.schema;

public class TableSpecificField {

	private int length;    				// Internal Length of this field
    private int oid;        			// OID of the type
    private String columnLabel; 		// Column label
    private String columnName;        	// Column name; null if undetermined
    private Integer nullable;        	// Is this column nullable? null if undetermined.
    private boolean autoIncrement;   	// Is this column automatically numbered?
    private int positionInTable;		// Position in table
    private int tableOid; 				// OID of table ( zero if no table )
    private String tableName;			// Table name;
    private String schema;				// Schema name;
    private int type;					// type 
    
    public TableSpecificField(String columnName){
		this.setColumnName(columnName);
		
    }
    
    /**
     * Construct a field based on the information fed to it.
     *
     * @param columnLabel the column label of the field
     * @param columnName the column label the name of the field
     * @param oid the OID of the field
     * @param length the length of the field
     * @param tableOid the OID of the columns' table
     * @param positionInTable the position of column in the table (first column is 1, second column is 2, etc...)
     */
    public TableSpecificField(String columnLabel, String columnName, int oid, int length, int tableOid, int positionInTable)
    {
    	this.setColumnName(columnName);
        this.columnLabel = columnLabel;
        this.oid = oid;
        this.length = length;
        this.setTableOid(tableOid);
        this.setPositionInTable(positionInTable);
    }
    
    /**
     * Comparazione tra due MetaField
     * @param c altro MetaField
     * @return vero se sono riferiti allo stesso campo
     */
    public boolean equals(TableSpecificField c){
    	
    	if ( !c.getColumnName().equals(this.columnName) ) return false;
    	if ( !c.getTableName().equals(this.tableName) ) return false;
    	
    	return true;
    }

	
    public String getColumnName() {
		return this.columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @param positionInTable the positionInTable to set
	 */
	public void setPositionInTable(int positionInTable) {
		this.positionInTable = positionInTable;
	}

	/**
	 * @return the positionInTable
	 */
	public int getPositionInTable() {
		return positionInTable;
	}

	/**
	 * @param tableOid the tableOid to set
	 */
	public void setTableOid(int tableOid) {
		this.tableOid = tableOid;
	}

	/**
	 * @return the tableOid
	 */
	public int getTableOid() {
		return tableOid;
	}

	/**
	 * @param i the type to set
	 */
	public void setType(int i) {
		this.type = i;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String toString(){
		return "["+this.columnName+"]";
	}
    
}
