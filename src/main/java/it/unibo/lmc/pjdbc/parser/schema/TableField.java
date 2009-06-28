package it.unibo.lmc.pjdbc.parser.schema;

public class TableField {

	/**
	 * Nome identificativo della colonna
	 */
	private String columnName;        			// Column name; null if undetermined
	
	
	/**
	 * Meta Informazioni
	 */
//	private int length;    						// Internal Length of this field
//    private int oid;        					// OID of the type
//    private String columnLabel; 				// Column label
//    private Integer nullable;        			// Is this column nullable? null if undetermined.
//    private boolean autoIncrement = false;   	// Is this column automatically numbered?
//    private int positionInTable;				// Position in table
//    private int tableOid; 						// OID of table ( zero if no table )
    private String tableName;					// Table name;
    private String schema;						// Schema name;
//    private int type;							// type 
    private boolean dinstinct;					// distinct field
    private String alias;
    
    public TableField(){
    	this.setColumnName("*");
    }
    
    public TableField(String columnName){
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
//    public TableField(String columnLabel, String columnName, int oid, int length, int tableOid, int positionInTable)
//    {
//    	this.setColumnName(columnName);
//        this.columnLabel = columnLabel;
//        this.oid = oid;
//        this.length = length;
//        this.setTableOid(tableOid);
//        this.setPositionInTable(positionInTable);
//    }
    
    /**
     * Comparazione tra due MetaField
     * @param c altro MetaField
     * @return vero se sono riferiti allo stesso campo
     */
    public boolean equals(TableField c){
    	
    	if ( !c.getColumnName().equals(this.columnName) ) return false;
    	
    	if ( this.tableName != null ){
    		if ( !c.getTableName().equals(this.tableName) ) return false;
    	} else {
    		if ( c.getTableName() != null ) return false;
    	}
    	
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

//	/**
//	 * @param positionInTable the positionInTable to set
//	 */
//	public void setPositionInTable(int positionInTable) {
//		this.positionInTable = positionInTable;
//	}
//
//	/**
//	 * @return the positionInTable
//	 */
//	public int getPositionInTable() {
//		return positionInTable;
//	}
//
//	/**
//	 * @param tableOid the tableOid to set
//	 */
//	public void setTableOid(int tableOid) {
//		this.tableOid = tableOid;
//	}
//
//	/**
//	 * @return the tableOid
//	 */
//	public int getTableOid() {
//		return tableOid;
//	}
//
//	/**
//	 * @param i the type to set
//	 */
//	public void setType(int i) {
//		this.type = i;
//	}
//
//	/**
//	 * @return the type
//	 */
//	public int getType() {
//		return type;
//	}

	/**
	 * @param tableName the tableName to set or alias tableName
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
	
	public void setDistinct(boolean b){
		this.dinstinct = true;
	}
    
	public boolean isDinstinct(){
		return this.dinstinct;
	}
	
	public String toString(){
		
		String aliasT = (null !=  this.alias) ? " AS " +this.alias : "";
		
		if ( null != this.tableName ) {
			if ( this.schema != null ) {
				return "["+ this.schema +"."+ this.tableName+"."+this.columnName+ aliasT + "]";
			} else {
				return "["+this.tableName+"."+this.columnName+ aliasT + "]";
			}
		} else {
			return "["+this.columnName+ aliasT + "]";
		}
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getAlias(){
		return this.alias;
	}

	public String getQualifiedName() {
		return this.schema+"."+this.tableName+"."+this.columnName;
	}
    
}
