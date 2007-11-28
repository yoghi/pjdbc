package net.prolog.jdbc.core;

public class Field {

	private final int length;    		// Internal Length of this field
    private final int oid;        		// OID of the type
    private final int mod;        		// type modifier of this field
    private final String columnLabel; 	// Column label
    private String columnName;        	// Column name; null if undetermined
    private Integer nullable;        	// Is this column nullable? null if undetermined.
    private Boolean autoIncrement;   	// Is this column automatically numbered?
    private final int positionInTable;	// Position in table
    private final int tableOid; 		// OID of table ( zero if no table )
    
    
    /*
     * Construct a field based on the information fed to it.
     *
     * @param columnLabel the column label of the field
     * @param columnName the column label the name of the field
     * @param oid the OID of the field
     * @param length the length of the field
     * @param tableOid the OID of the columns' table
     * @param positionInTable the position of column in the table (first column is 1, second column is 2, etc...)
     */
    public Field(String columnLabel, String columnName, int oid, int length, int mod, int tableOid, int positionInTable)
    {
        this.columnLabel = columnLabel;
        this.columnName = columnName;
        this.oid = oid;
        this.length = length;
        this.mod = mod;
        this.tableOid = tableOid;
        this.positionInTable = positionInTable;
    }
    
}
