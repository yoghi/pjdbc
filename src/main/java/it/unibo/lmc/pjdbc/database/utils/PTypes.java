package it.unibo.lmc.pjdbc.database.utils;
	
public enum PTypes {
	
	STRING (java.sql.Types.VARCHAR),
	INT ( java.sql.Types.INTEGER ),
	ARRAY ( java.sql.Types.ARRAY ),
	NULL ( java.sql.Types.NULL ),
	REAL ( java.sql.Types.REAL );

	
	private final int jType;
	
	PTypes(int javaType) {
        this.jType = javaType;
    }
	
	public int getSqlType(){
		return this.jType;
	}
}
